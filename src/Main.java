public class Main {

    public static void main(String[] args) {
        System.out.println("Sistema de invernadero inteligente");

        // 1. Creación de objetos mediante los constructores.
        //    Cada subclase llama a super(...) para fijar su unidad.
        SensorTemperatura temperatura = new SensorTemperatura("T-01", "Zona norte");
        SensorHumedad humedad = new SensorHumedad("H-01", "Zona central");
        SensorHumedadSuelo humedadSuelo = new SensorHumedadSuelo("S-01", "Zona sur");

        // getId(), getUbicacion(), getUnidad() e isActivo() están escritos en Sensor:
        // las subclases los heredan.
        System.out.println();
        System.out.println("=== 1. Sensores creados (constructores, super y métodos heredados) ===");
        System.out.println(temperatura.getId() + " | " + temperatura.getUbicacion() + " | unidad: " + temperatura.getUnidad() + " | activo: " + temperatura.isActivo());
        System.out.println(humedad.getId() + " | " + humedad.getUbicacion() + " | unidad: " + humedad.getUnidad() + " | activo: " + humedad.isActivo());
        System.out.println(humedadSuelo.getId() + " | " + humedadSuelo.getUbicacion() + " | unidad: " + humedadSuelo.getUnidad() + " | activo: " + humedadSuelo.isActivo());

        // 2 a 4. Cada sensor interpreta sus mediciones con su propia versión
        //        de evaluarMedicion() (sobrescritura).
        System.out.println();
        System.out.println("=== 2. Sensor de temperatura: baja, adecuada y alta ===");
        double[] temperaturas = {15, 25, 34};
        for (double valor : temperaturas) {
            temperatura.registrarMedicion(valor);
            System.out.println(temperatura.obtenerResumen());
        }

        System.out.println();
        System.out.println("=== 3. Sensor de humedad ambiental: baja, adecuada y alta ===");
        double[] humedades = {32, 55, 82};
        for (double valor : humedades) {
            humedad.registrarMedicion(valor);
            System.out.println(humedad.obtenerResumen());
        }

        System.out.println();
        System.out.println("=== 4. Sensor de humedad del suelo: baja, adecuada y alta ===");
        double[] humedadesSuelo = {18, 48, 83};
        for (double valor : humedadesSuelo) {
            humedadSuelo.registrarMedicion(valor);
            System.out.println(humedadSuelo.obtenerResumen());
        }

        // 5. El mismo número significa cosas distintas según el tipo de sensor.
        System.out.println();
        System.out.println("=== 5. Mismo valor (32), distinta interpretación ===");
        temperatura.registrarMedicion(32);
        humedad.registrarMedicion(32);
        humedadSuelo.registrarMedicion(32);
        System.out.println(temperatura.getId() + ": " + temperatura.evaluarMedicion());
        System.out.println(humedad.getId() + ": " + humedad.evaluarMedicion());
        System.out.println(humedadSuelo.getId() + ": " + humedadSuelo.evaluarMedicion());

        // 6. Encapsulación: la medición solo cambia mediante registrarMedicion(),
        //    que aplica las reglas del sensor.
        System.out.println();
        System.out.println("=== 6. Reglas al registrar mediciones (encapsulación) ===");
        boolean registrada = humedad.registrarMedicion(150);
        System.out.println("H-01 intenta registrar 150 %: " + registrada + " | última medición: " + humedad.getUltimaMedicion() + " %");

        temperatura.desactivar();
        registrada = temperatura.registrarMedicion(20);
        System.out.println("T-01 inactivo intenta registrar 20 °C: " + registrada + " | última medición: " + temperatura.getUltimaMedicion() + " °C");
        System.out.println(temperatura.obtenerResumen());

        temperatura.activar();
        System.out.println("T-01 activado de nuevo:");
        System.out.println(temperatura.obtenerResumen());

        // 7. Comportamientos que solo tiene un tipo de sensor.
        System.out.println();
        System.out.println("=== 7. Comportamientos propios de una subclase ===");
        System.out.println("T-01: " + temperatura.getUltimaMedicion() + " °C = "
                + temperatura.convertirAFahrenheit() + " °F");

        humedadSuelo.registrarMedicion(22);
        System.out.println("S-01 con " + humedadSuelo.getUltimaMedicion() + " %: ¿requiere riego? " + humedadSuelo.requiereRiego());
        humedadSuelo.registrarMedicion(55);
        System.out.println("S-01 con " + humedadSuelo.getUltimaMedicion() + " %: ¿requiere riego? " + humedadSuelo.requiereRiego());
    }

}