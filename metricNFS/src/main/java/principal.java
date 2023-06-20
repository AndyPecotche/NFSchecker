import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class principal {
	LocalDateTime locaDate = LocalDateTime.now();
	//static String Path;
	//static String logFilePath = "registro.log";
	//static FileWriter writer;
	static String path;
	static int port;
	
    private static final Counter countMetricOK = Counter.build()
            .name("NFSmount_Funcionando_metric_count")
            .help("Si el contador sube en razon de 1 por segundo, el montaje responde")
            .register();
    
    private static final Counter countMetricNoOK = Counter.build()
            .name("NFSmount_failing_metric_count_")
            .help("Si el contador sube significa que el montaje no respondio por el tiempo determinado segun el parametro del montaje, y el sistema arrojo error")
            .register();

    public static void main(String[] args) throws IOException, InterruptedException {

       leerVariables("config.txt");
       System.out.println("ruta con archivo de control:" + path);
       if (port<=0 || port>65535) { 
    	   System.out.println("elija un puerto valido");System.exit(0);
       }
       try {
    		   HTTPServer server = new HTTPServer(port);
       }catch (Exception e) {
    		   System.out.println("elija otro puerto");
    		   e.printStackTrace();
    		   System.exit(0);
       }
        // Programar una tarea que se ejecutará cada 1 segundo
       // TimerTask task = new TimerTask() {
      //      @Override
  //          public void run() {
       
    	boolean vale = false;
    	try { 
        	String fileContent = readFileContent(path);
        	vale = true;
        }catch (Exception e){
        	System.out.println("la ruta con archivo de control:" + path + " no existe");
        	vale = false;
        	System.exit(0);
        }
        
        while (vale) {
           	leer();
            Thread.sleep(1000);
        }
        
     //   };

      //  Timer timer = new Timer();
       // timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private static String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
    
    /*
    private static String readConfigFile() throws IOException {
        Path configPath = Paths.get("config.txt");
        String filePath = Files.readString(configPath).trim();
        return new String(filePath);
    } */
    private static void leerVariables(String configPath) {
    
    String content = readFile(configPath); // Leer el archivo de texto

    String[] lines = content.split("\n"); // Dividir el contenido en líneas

    	
    path = "";
    port = 0;

    // Iterar sobre cada línea y asignar los valores correspondientes
    for (String line : lines) {
        String[] parts = line.split("=");

        if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.equals("path")) {
                path = value;
            } else if (key.equals("port")) {
                try {
                    port = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.out.println("El valor del puerto no es válido.");
                }
            }
        }
    }
    
    // Imprimir los valores asignados
    System.out.println("Path: " + path);
    System.out.println("Port: " + port);
}

    private static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    
  /*  private static void writeLog(String logMessage) throws IOException {
        try {
            writer.write(logMessage);
            writer.write(System.lineSeparator());
            writer.flush();
        }catch (Exception e) {
        	
        }
    } */
    
    private static void leer() throws IOException{
    	try {
            // Leer el contenido del archivo
            String fileContent = readFileContent(path);
            // Comparar el contenido del archivo con el valor esperado
            //if (fileContent.equals(expectedContent)) {
          //  Incrementar la métrica de conteo (count)
            countMetricOK.inc();
            System.out.println(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ": Responde");
            //}
        } catch (Exception e) {
        	countMetricNoOK.inc();
        	System.out.println(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ": No responde");
        	e.printStackTrace();
        }
    }
}
