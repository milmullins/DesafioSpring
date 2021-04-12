package com.example.DesafioSpring.services;

/*import com.example.desafioSpring.dto.*;
import com.example.desafioSpring.exceptions.ArticleNotFoundException;
import com.example.desafioSpring.exceptions.InvalidFilterException;
import com.example.desafioSpring.exceptions.OutOfStockException;
import com.example.desafioSpring.repositories.ArticleRepository;*/
import com.example.DesafioSpring.dto.*;
import com.example.DesafioSpring.exceptions.ArticleNotFoundException;
import com.example.DesafioSpring.exceptions.InvalidFilterException;
import com.example.DesafioSpring.exceptions.OutOfStockException;
import com.example.DesafioSpring.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    private final ResponseDTO shoppingCart = new ResponseDTO();

    //metodo que busca todos los productos si no se le pasan FILTROS
    //si se le pasan filtros se valida que los filtros pasados sean correctos si no devuelve una excepcion
    @Override
    public List<ArticleDTO> searchProducts(Map<String, String> filters) throws IOException, InvalidFilterException {
        if (filters != null) {
            if (isFilterValid(filters))
                return filterProducts(filters);
            else
                throw new InvalidFilterException(HttpStatus.BAD_REQUEST, "Los Filtros aplicados son incorrectos.");
        } else
            return articleRepository.searchProducts();
    }

    private boolean isFilterValid(Map<String, String> filters) {
        //remuevo en este scope el filtro de orden para controlar si es valido
        for (String filter : filters.keySet()) {
            if (filter.equals("order")) {
                filters.remove("order");
            }
        }
        //si la cantidad de filtros pasados es mayor a 2 devuelve excepcion
        if (filters.keySet().size() > 2)
            return false;
        //si le paso algun filtro que no esta habilitado devuelve excepcion
        List<String> enabledFilters = new ArrayList<>(Arrays.asList("category", "brand", "name", "price", "freeShipping", "prestige"));
        for (String filter : filters.keySet()) {
            if (!enabledFilters.contains(filter)) {
                return false;
            }
        }

        return true;
    }

    //metodo para realizar una compra
    //recive una lista de articulos y devuelve un ticket
    @Override
    public ResponseDTO purchaseRequest(Articles articles) throws IOException {
        List<ArticleDTO> productListBD = articleRepository.searchProducts();
        double total = 0;
        boolean artricleExist = false;
        int stock = 0;

        //controlo si los articulos solicitados se encuentran en la base de datos
        for (ArticleResponseDTO art : articles.getArticles()) {
            for (ArticleDTO articuloBD : productListBD) {
                if (articuloBD.getProductId() == art.getProductId()) {
                    artricleExist = true;
                    break;
                }
            }
            if (!artricleExist) {
                throw new ArticleNotFoundException(HttpStatus.NOT_FOUND, "Articulo inexistente.");
            }
            artricleExist = false;
        }

        //controlo si hay disponibilidad de Stock para los articulos solicitados
        for (ArticleResponseDTO art : articles.getArticles()) {
            for (ArticleDTO articuloBD : productListBD) {
                if (articuloBD.getProductId() == art.getProductId()) {
                    if (articuloBD.getQuantity() >= art.getQuantity()) {
                        total += art.getQuantity() * articuloBD.getPrice();
                        stock = articuloBD.getQuantity() - art.getQuantity();
                        articleRepository.updateCSV(String.valueOf(art.getProductId()), String.valueOf(articuloBD.getQuantity()), String.valueOf(stock));
                    } else {
                        throw new OutOfStockException(HttpStatus.OK, "Stock insuficiente para el articulo: " + art.getName());
                    }
                }
            }
        }
        //genero un nuevo ticket y status code que conforman la respuesta solicitada
        TicketDTO ticket = new TicketDTO(articles.getArticles(), total);
        StatusCodeDTO statusCode = new StatusCodeDTO(HttpStatus.OK.value(), "La solicitud de compra se completó con éxito");
        return new ResponseDTO(ticket, statusCode);
    }

    //Metodo que funciona como carrito de compra sumando los diferentes pedidos
    //devuelve un ticket que contiene la lista de articulos solicitados y el total
    @Override
    public ResponseDTO shoppingCart(Articles articles) throws IOException {

        boolean articleExist = false;
        ResponseDTO cart = purchaseRequest(articles);
        //Inicializo el carro de compra por primera vez
        if (shoppingCart.getTicket() == null || shoppingCart.getStatusCode() == null) {
            shoppingCart.setTicket(cart.getTicket());
            shoppingCart.setStatusCode(cart.getStatusCode());
        } else {
            //Recorro la lista NUEVA de articulos a sumar
            for (ArticleResponseDTO art : cart.getTicket().getArticles()) {
                //Recorro la lista de articulos ques ya estan añadidos para actualizar la cantidad
                for (ArticleResponseDTO artInCart : shoppingCart.getTicket().getArticles()) {
                    if (art.getProductId() == artInCart.getProductId()) {
                        artInCart.setQuantity(artInCart.getQuantity() + art.getQuantity());
                        articleExist = true;
                    }
                }
                if(!articleExist)
                    shoppingCart.getTicket().getArticles().add(art);
            }
            double total = shoppingCart.getTicket().getTotal() + cart.getTicket().getTotal();
            shoppingCart.getTicket().setTotal(total);
        }
        return shoppingCart;
    }

    //metodo que aplica el filtro a la lista de productos
    private List<ArticleDTO> filterProducts(Map<String, String> filter) throws IOException {
        List<ArticleDTO> productList = articleRepository.searchProducts();
        List<ArticleDTO> fProductList = new ArrayList<>(productList);
        boolean ordenar = false;
        int tipoOrden = -1;

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            switch (entry.getKey()) {
                case "category":
                    for (ArticleDTO product : productList) {
                        if (!product.getCategory().equalsIgnoreCase(entry.getValue()))
                            fProductList.remove(product);
                    }
                    break;
                case "brand":
                    for (ArticleDTO product : productList) {
                        if (!product.getBrand().equalsIgnoreCase(entry.getValue())) {
                            fProductList.remove(product);
                        }
                    }
                    break;
                case "name":
                    for (ArticleDTO product : productList) {
                        if (!product.getName().equalsIgnoreCase(entry.getValue())) {
                            fProductList.remove(product);
                        }
                    }
                    break;
                case "price":
                    for (ArticleDTO product : productList) {
                        if (product.getPrice() != (Double.parseDouble(entry.getValue()))) {
                            fProductList.remove(product);
                        }
                    }
                    break;
                case "freeShipping":
                    for (ArticleDTO product : productList) {
                        if (product.isFreeShipping() != (entry.getValue().equals("true"))) {
                            fProductList.remove(product);
                        }
                    }
                    break;
                case "prestige":
                    for (ArticleDTO product : productList) {
                        if (product.getPrestige() != Integer.parseInt(entry.getValue())) {
                            fProductList.remove(product);
                        }
                    }
                    break;
                case "order":
                    ordenar = true;
                    tipoOrden = Integer.parseInt(entry.getValue());
                    break;
            }
            productList = new ArrayList<>(fProductList);
        }

        if (ordenar)
            fProductList = sortList(fProductList, tipoOrden);

        return fProductList;
    }

    //metodo para ordenar la lista segun el tipo de orden que se seleccione
    public List<ArticleDTO> sortList(List<ArticleDTO> listArticles, int tipoOrden) {
        switch (tipoOrden) {
            case 0:
                Collections.sort(listArticles);
                break;
            case 1:
                listArticles.sort(Collections.reverseOrder());
                break;
            case 2:
                listArticles.sort(new ArticlePriceComparator());
                break;
            case 3:
                listArticles.sort(Collections.reverseOrder(new ArticlePriceComparator()));
                break;
        }
        return listArticles;
    }
}
