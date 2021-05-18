package data;

import Interfaces.IProduction;
import Interfaces.IRightsholder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

class ProductionHandler {
    private File file;

    private static Connection connection = null;

    public ProductionHandler() {
        try {
            connection = getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Establish a connection to the Database
     * @return Connection
     */
    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        return DriverManager.getConnection(
                "jdbc:postgresql://ec2-34-250-16-127.eu-west-1.compute.amazonaws.com:5432/d82q285u8akq13",
                "pjmqbdledqjucs",
                "030c8df346d06432b9a5a4ed0bf42e56dc34761fda8d3cc04af8085dfb4f7c2b");
    }


    // Singleton
    private final static ProductionHandler prHandler = new ProductionHandler("productionData");

    private ProductionHandler(String fileName) {
        try {
            this.file = new File(getClass().getResource("/" + fileName + ".dat").toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Reads the whole productionData file and returns all productions in an arraylist;
    List<IProduction> readPFile() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(this.file);
            List<IProduction> productions = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(";");
                productions.add(new Production(line[0], line[1], convertToMap(line[2])));
            }
            return productions;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return null;
    }

    // Backup of old method - DO NOT DELETE FOR NOW
    /*
    public Map<IRightsholder, List<String>> convertToMap(String rhRoles) {
        String[] rightholdersWithRoles = rhRoles.split("¤");
        Map<IRightsholder, List<String>> map = new HashMap<>();
        RightsHolderHandler rhandler = RightsHolderHandler.getInstance();
        for (String rhRole : rightholdersWithRoles) {
            String[] splitted = rhRole.split(":");
            List<String> roles = new ArrayList<>(Arrays.asList(splitted[1].replace("[", "").replace("]", "").split(",")));
            map.put(rhandler.readRightsholder(Integer.parseInt(splitted[0])), roles);
        }
        return map;
    }
     */

    // Converting the string format from the file to a map.
    Map<IRightsholder, List<String>> convertToMap(String rhRoles) {
        Map<IRightsholder, List<String>> map = new HashMap<>();
        RightsHolderHandler rhandler = RightsHolderHandler.getInstance();
        String[] splitted = rhRoles.split(":");
        List<String> roles = new ArrayList<>(Arrays.asList(splitted[1].replace("[", "").replace("]", "").split(",")));
        map.put(rhandler.readRightsholder(Integer.parseInt(splitted[0])), roles);
        return map;
    }

    // Reads specific production with productionID
    IProduction readProduction(String id) {
        List<IProduction> productions = readPFile();
        for (IProduction p : productions) {
            if (((Production) p).getProductionID().equals(id)) {
                return p;
            }
        }
        return null;
    }

    void saveProduction(IProduction production) {
        if (!(production instanceof Production)) {
            production = new Production(production.getProductionID(), production.getName(), production.getRightsholders());
        }

        List<IProduction> readings = readPFile();
        boolean contains = false;
        for (int i = 0; i < readings.size(); i++) {
            if (readings.get(i).getProductionID().equals(production.getProductionID())) {
                contains = true;
                readings.remove(i);
                break;
            }
        }
        if (contains) {
            readings.add(production);
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(this.file);
                fileWriter.append(readings.get(0).getProductionID() + ";" + readings.get(0).getName() + ";" + ((Production) readings.get(0)).mapToString());
                for (int i = 1; i < readings.size(); i++) {
                    fileWriter.append("\n" + readings.get(i).getProductionID() + ";" + readings.get(i).getName() + ";" + ((Production) readings.get(i)).mapToString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(this.file, true);

                if (readings.size() == 0) {
                    fileWriter.write(production.getProductionID() + ";" + production.getName() + ";" + ((Production) production).mapToString());
                } else {
                    fileWriter.write("\n" + production.getProductionID() + ";" + production.getName() + ";" + ((Production) production).mapToString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    boolean deleteProduction(IProduction production) {
        List<IProduction> readings = readPFile();
        boolean contains = false;
        for (int i = 0; i < readings.size(); i++) {
            if (readings.get(i).getProductionID().equals(production.getProductionID())) {
                contains = true;
                readings.remove(i);
                break;
            }
        }
        if (contains) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(this.file);
                if (readings.size() >= 1) {
                    fileWriter.append(readings.get(0).getProductionID() + ";" + readings.get(0).getName() + ";" + ((Production) readings.get(0)).mapToString());
                    for (int i = 1; i < readings.size(); i++) {
                        fileWriter.append("\n" + readings.get(i).getProductionID() + ";" + readings.get(i).getName() + ";" + ((Production) readings.get(i)).mapToString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return contains;
    }

    static ProductionHandler getInstance() {
        return prHandler;
    }
}
