# Proyecto final para PMDM

## Descripción

La API elegida para el proyecto ha sido APOD. Esta API es una de las múltiples que ofrece la NASA, concretamente se trata de un repositorio el cual almacena una fotografía diaria. Cada registro cuenta con el título de la foto, la fecha de la foto, y enlaces a la foto tanto en calidad básica como calidad HD.

## Bloques de la aplicación

La aplicación consta de 4 bloques principales.

### 1. Pantalla de inicio
Para la pantalla de inicio se ha optado por establecer como fondo de la aplicación, la foto correspondiente a la fecha actual un año atrás. La llamada a la API se hace restando 365 a la fecha de hoy. En esta pantalla de inicio también contamos con distintos botones, los cuales enlazan con el resto de bloques principales, y un botón de información el cuál nos muestra el título y la fecha de la foto de la pantalla principal.

### 2. Hoy
Desde la pantalla principal tenemos el botón "HOY". Este botón nos llevara a su propio layout el cual se compone de la foto a fecha de hoy, el título de la foto, la fecha y la descripción. Pulsando sobre la imagen se cargará la foto en HD.

### 3. Esta semana
Otro de los botones es "ESTA SEMANA". Con este botón accederemos a una vista compuesta por un recycler view, con el cuál podremos ver las entradas de la última semana. La vista individual utilizada para el recycler view es la misma que la vista utilizada para 'Hoy'.

### 4. Elegir fecha
Con este botón, tras previamente haber elegido una fecha con el widget de pickNumber, podremos acceder a la foto de la fecha elegida. La vista utilizada es la misma que la utilizada en 'Hoy'. La aplicación previene el uso de fechas no válidas, previas a la fecha mínima o posteriores a la fecha actual. 
