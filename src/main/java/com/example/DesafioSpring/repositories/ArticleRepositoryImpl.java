package com.example.DesafioSpring.repositories;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.example.DesafioSpring.dto.ArticleDTO;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    @Override
    public List<ArticleDTO> searchProducts() throws IOException {
        return loadDataBase();
    }

    private List<ArticleDTO> loadDataBase() throws IOException {
        List<ArticleDTO> productList = new ArrayList<>();

        try {
            //Si se cambia el archivo posee otro nombre devuelve una Excepcion
            File inputF = new File("src/main/resources/dbProductos.csv");
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            // Saltea la primer linea que son los nombres de las columnas.
            productList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
        //devuelve la lista entera de productos de la BD
        return productList;
    }

    private final Function<String, ArticleDTO> mapToItem = (line) -> {
        //cada linea del csv se separa en un array que me permite cargar los datos de cada producto.
        String[] producto = line.replace("\"", "").split(",");

        ArticleDTO item = new ArticleDTO();
        item.setProductId(Integer.parseInt(producto[0]));
        item.setName(producto[1]);
        item.setCategory(producto[2]);
        item.setBrand(producto[3]);
        item.setPrice(Double.parseDouble(producto[4].substring(1).replace(".", "")));
        item.setQuantity(Integer.parseInt(producto[5]));
        item.setFreeShipping(producto[6].equals("SI"));
        item.setPrestige(producto[7].toCharArray().length);

        return item;
    };

    @Override
    public void updateCSV(String idProducToActualice, String quantityOld, String quantityNew) throws IOException {
        //busco el archivo csv a editar
        File inputFile = new File("src/main/resources/dbProductos.csv");
        // se lee el archivo
        CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
        List<String[]> csvBody = reader.readAll();
        // salteo la primer linea y busco la linea correspondiente al producto y le edito la cantidad
        for (int i = 1; i < csvBody.size(); i++) {
            String[] strArray = csvBody.get(i);
            if (strArray[0].equalsIgnoreCase(idProducToActualice)) {
                if (strArray[5].equalsIgnoreCase(quantityOld)) { //controlo que la cantidad pasada sea la correcta
                    csvBody.get(i)[5] = quantityNew; //reemplazo por el stock sobrante
                }
            }
        }
        reader.close();

        // cargo nuevamente el csv
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

}
