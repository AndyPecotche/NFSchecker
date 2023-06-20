# NFSchecker
Aplicacion en java para generar una metrica de prometheus que sube un contador a razon de 1 cada segundo en caso de poder leer el contenido de un directorio especificado.
Pensado para utilizarse cuando se tiene un directorio montado por NFS, el cual si el servidor NFS se desconecta simplemente no responde (montaje hard) o responde con un error (motaje soft), en este segundo caso tambien se cuantifica con una metrica que incrementa a medida aumentan las respuestas con error, esto puede ser util ya que si el servidor no responde, la razon a la que crece el contador sera mucho menor a que si el servidor responde pero no se tiene montado directamente el directorio por ejemplo y por tanto no encuentra el archivo (en este caso la razon sera de 1 unidad por cada segundo).

El archivo config.txt debe mantener ese nombre y quedar conjunto al .jar
El formato del archivo config.txt debe mantenerse como se encuentra. (no admite comentarios)
