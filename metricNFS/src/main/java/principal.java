import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class principal {
	LocalDateTime locaDate = LocalDateTime.now();
	static String filePath;
	static String logFilePath = "registro.log";
	static FileWriter writer;
	
    private static final Counter countMetricOK = Counter.build()
            .name("NFSmount_Funcionando_metric_count")
            .help("Number of times file content matches expected value")
            .register();
    
    //private static final Counter countMetricNoOK = Counter.build()
           // .name("NFSmount_failing_metric_count_")
          //  .help("Number of times file content matches expected value")
          //  .register();

    public static void main(String[] args) throws IOException, InterruptedException {
        // Iniciar el servidor Prometheus
    	
    	try {
    		writer = new FileWriter(logFilePath, true);
    	}catch (Exception e){
    		System.out.println("no se puede usar el log");
    	}
    	
        try {
               filePath = readConfigFile();
               writeLog("ruta con archivo de control:" + filePath);
            }catch (Exception e){
               //filePath = "/Web/WebSAP/carpetaControl/controlFile";
               System.out.println("error al leer el path desde el arhivo config.txt");
            }
        HTTPServer server = new HTTPServer(8081);

        // Programar una tarea que se ejecutará cada 1 segundo
       // TimerTask task = new TimerTask() {
      //      @Override
  //          public void run() {
        boolean vale = false;
        try { 
        	String fileContent = readFileContent(filePath);
        	vale = true;
        }catch (Exception e){
        	System.out.println("la ruta con archivo de control:" + filePath + " no existe");
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
    
    private static String readConfigFile() throws IOException {
        Path configPath = Paths.get("config.txt");
        String filePath = Files.readString(configPath).trim();
        return new String(filePath);
    }
    
    private static void writeLog(String logMessage) throws IOException {
        try {
            writer.write(logMessage);
            writer.write(System.lineSeparator());
            writer.flush();
        }catch (Exception e) {
        	
        }
    }
    
    private static void leer() throws IOException{
    	try {
            // Leer el contenido del archivo
            String fileContent = readFileContent(filePath);
            // Comparar el contenido del archivo con el valor esperado
            //if (fileContent.equals(expectedContent)) {
          //  Incrementar la métrica de conteo (count)
            countMetricOK.inc();
            writeLog(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ": Responde");
            //}
        } catch (Exception e) {
        	//countMetricNoOK.inc();
        	writeLog(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ": No responde");
        	writeLog(e.printStackTrace());
        }
    }
}
