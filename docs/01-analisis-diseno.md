# Análisis y diseño

## 1. Descripción del problema

Voy a representar, mediante una aplicación de consola en Java, un **invernadero inteligente simplificado**. El sistema observa tres variables ambientales (temperatura del aire, humedad ambiental y humedad del suelo) por medio de sensores, interpreta cada medición según el tipo de variable y, a partir de la humedad del suelo, decide si el sistema de riego debe activarse o quedarse inactivo.

El funcionamiento general sigue tres pasos:

```text
OBSERVAR  →  EVALUAR  →  ACTUAR
(sensores)   (cada sensor     (sistema de riego)
              interpreta su
              medición)
```

### Información que el sistema necesita manejar

- Datos de cada sensor: una forma de identificarlo, el lugar donde está instalado (por ejemplo, "zona norte") y si está activo o inactivo.
- La última medición de cada sensor. Las mediciones son **simuladas** desde el programa, no vienen de sensores físicos.
- La unidad de cada medición: °C para la temperatura y % para las humedades.
- Los criterios para interpretar cada medición, que son **distintos para cada variable**:

| Variable | Unidad | Baja | Adecuada | Alta |
|---|---|---|---|---|
| Temperatura del aire | °C | menor a 18 | 18 a 30 | mayor a 30 |
| Humedad ambiental | % | menor a 40 | 40 a 70 | mayor a 70 |
| Humedad del suelo | % | menor a 30 | 30 a 70 | mayor a 70 |

- El estado del sistema de riego: activo (suministrando agua) o inactivo (riego detenido).

### Elementos que intervienen

- El invernadero, como el espacio que agrupa a todos los demás elementos.
- Tres tipos de sensores: de temperatura, de humedad ambiental y de humedad del suelo.
- El sistema de riego.

### Operaciones generales

1. Registrar (simular) una medición en cada sensor.
2. Consultar la información de los sensores.
3. Interpretar cada medición de acuerdo con el tipo de sensor.
4. Decidir, con base en la humedad del suelo, si es necesario regar.
5. Activar, desactivar y consultar el estado del sistema de riego.
6. Mostrar en consola el estado general del invernadero.

### Observación importante

Un número por sí solo no dice nada. El valor `32` significa cosas diferentes dependiendo de quién lo mide:

```text
Sensor de temperatura       → 32 °C → temperatura alta
Sensor de humedad ambiental → 32 %  → humedad ambiental baja
Sensor de humedad del suelo → 32 %  → humedad del suelo adecuada
```

Por eso la interpretación de una medición tiene que estar ligada al tipo de sensor que la produce, y no a un lugar central que reciba solo el número.

---

## 2. Identificación de objetos

### Sensor (concepto general)

- **Qué representa:** cualquier dispositivo del invernadero que observa una variable ambiental.
- **Por qué debe existir como objeto:** tiene información propia (identificación, ubicación, estado y última medición) y comportamientos propios (registrar una medición, informarla e interpretarla). Además, es el concepto que agrupa lo que tienen en común todos los sensores.
- **Responsabilidad:** observar una variable y conservar su última medición.

> En el invernadero no existe un "sensor genérico" físico; todos son de algún tipo concreto. Aun así, el concepto general es útil para no repetir lo que comparten.

### Sensor de temperatura

- **Qué representa:** el sensor que mide la temperatura del aire dentro del invernadero.
- **Por qué debe existir como objeto:** es un sensor, pero mide en °C e interpreta sus mediciones con sus propios rangos.
- **Responsabilidad:** registrar la temperatura del aire e indicar si es baja, adecuada o alta.

### Sensor de humedad ambiental

- **Qué representa:** el sensor que mide la humedad presente en el aire.
- **Por qué debe existir como objeto:** es un sensor con unidad % y criterios de interpretación propios.
- **Responsabilidad:** registrar la humedad ambiental e indicar si es baja, adecuada o alta.

### Sensor de humedad del suelo

- **Qué representa:** el sensor que estima qué tan húmedo o seco está el suelo de las plantas.
- **Por qué debe existir como objeto:** es un sensor con criterios propios y, además, su medición es la que se usa para decidir el riego.
- **Responsabilidad:** registrar la humedad del suelo, interpretarla e indicar si el suelo necesita riego.

### Sistema de riego

- **Qué representa:** el mecanismo que suministra agua a las plantas.
- **Por qué debe existir como objeto:** tiene un estado propio (activo o inactivo) que cambia durante la ejecución y acciones propias (activarse y desactivarse).
- **Responsabilidad:** ejecutar o detener el riego. No mide ni interpreta nada; solamente actúa.

### Invernadero

- **Qué representa:** el espacio que contiene a los sensores y al sistema de riego.
- **Por qué debe existir como objeto:** alguien tiene que coordinar a los demás elementos: pedir a los sensores que evalúen sus mediciones y, con la información del sensor de humedad del suelo, decidir si se activa el riego. Si no existiera, esa lógica terminaría mezclada en `Main` o dentro de los sensores o del sistema de riego, y ninguno de ellos debería tener esa responsabilidad.
- **Responsabilidad:** agrupar los sensores y el sistema de riego y coordinar la decisión de riego.

### Elementos que decidí no representar como objetos (por ahora)

| Elemento | Motivo |
|---|---|
| Medición | Un valor numérico es suficiente; su significado lo da el sensor que la produce. Podría convertirse en objeto si después se necesitara guardar fecha, hora o un historial. |
| Interpretación (baja/adecuada/alta) | Es el resultado que devuelve el sensor, no algo con comportamiento propio. |
| Rangos de interpretación | Son datos fijos de cada tipo de sensor. |
| Plantas, agua, bomba, tuberías | Quedan fuera del alcance; el documento de contexto indica que no son necesarios. |

---

## 3. Estado y comportamiento

| Objeto propuesto | Responsabilidad | Información que debe conservar | Comportamientos que debe realizar |
|---|---|---|---|
| Sensor (general) | Observar una variable y conservar su última medición | Identificación, ubicación, si está activo o inactivo, última medición | Permitir conocer su identificación, ubicación y estado. Activarse y desactivarse. Registrar una nueva medición simulada. Permitir conocer su última medición. Interpretar su medición. Mostrar su información. |
| Sensor de temperatura | Observar la temperatura del aire | Lo mismo que cualquier sensor, más su unidad (°C) y sus rangos (18 y 30 °C) | Interpretar su medición como temperatura baja, adecuada o alta. Indicar su unidad. Podría expresar la temperatura en otra escala, por ejemplo °F. |
| Sensor de humedad ambiental | Observar la humedad del aire | Lo mismo que cualquier sensor, más su unidad (%) y sus rangos (40 y 70 %) | Interpretar su medición como humedad ambiental baja, adecuada o alta. Indicar su unidad. |
| Sensor de humedad del suelo | Observar la humedad del suelo | Lo mismo que cualquier sensor, más su unidad (%) y sus rangos (30 y 70 %) | Interpretar su medición como humedad del suelo baja, adecuada o alta. Indicar su unidad. Indicar si el suelo necesita riego. |
| Sistema de riego | Ejecutar o detener el suministro de agua | Identificación o zona donde riega, estado (activo o inactivo) | Activarse. Desactivarse. Permitir consultar su estado. Mostrar su estado. |
| Invernadero | Agrupar y coordinar los elementos del sistema | Nombre, conjunto de sensores, sistema de riego | Agregar sensores. Pedir a cada sensor que interprete su medición. Decidir, con la información del sensor de humedad del suelo, si el riego se activa o se desactiva. Mostrar un resumen del estado general. |

### Supuestos que tomé

- Un sensor inactivo no debería registrar mediciones nuevas ni tomarse en cuenta para decidir el riego.
- Las mediciones de humedad solo tienen sentido entre 0 % y 100 %.
- El sistema de riego no decide por sí mismo cuándo regar; recibe la indicación de activarse o desactivarse.

---

## 4. Características comunes y especialización

### 1. ¿Qué información tienen en común?

Los tres sensores tienen una identificación, una ubicación, un estado (activo o inactivo) y una última medición. Todos tienen además una unidad, aunque el valor de esa unidad no sea el mismo.

### 2. ¿Qué comportamientos tienen en común?

Todos pueden registrar una medición, permitir consultarla, activarse o desactivarse, mostrar su información e **interpretar su medición**.

### 3. ¿Qué características cambian dependiendo del tipo de sensor?

- La unidad (°C o %).
- Los rangos con los que se interpreta la medición.
- El resultado de la interpretación ("temperatura alta" no es lo mismo que "humedad del suelo baja").
- Algunos comportamientos que solo tienen sentido para un tipo, por ejemplo indicar si el suelo necesita riego (solo humedad del suelo) o expresar la temperatura en °F (solo temperatura).

Lo más importante que noté es que **interpretar la medición es un comportamiento común, pero la forma de hacerlo cambia en cada tipo de sensor**. Todos lo hacen, pero cada uno a su manera.

| Característica | Temperatura | Humedad ambiental | Humedad del suelo | ¿Común o particular? |
|---|---|---|---|---|
| Identificación | Sí | Sí | Sí | Común |
| Ubicación | Sí | Sí | Sí | Común |
| Activo / inactivo | Sí | Sí | Sí | Común |
| Última medición | Sí | Sí | Sí | Común |
| Interpretar la medición | Sí | Sí | Sí | Común, pero cada uno lo hace distinto |
| Unidad | °C | % | % | Particular |
| Rangos | 18 / 30 | 40 / 70 | 30 / 70 | Particular |
| Indicar si requiere riego | No | No | Sí | Particular |

### 4. ¿Existe un concepto general que permita representar a todos los sensores?

Sí: el **sensor**. Concentra la información y los comportamientos que comparten los tres tipos.

### 5. ¿Qué elementos podrían representar especializaciones de ese concepto?

El sensor de temperatura, el sensor de humedad ambiental y el sensor de humedad del suelo. Cada uno **es un** sensor, pero agrega su propia unidad, sus propios rangos y su propia forma de interpretar.

```text
                  Sensor
             (lo que es común)
                    │
      ┌─────────────┼─────────────┐
      │             │             │
 Temperatura   Hum. ambiental  Hum. suelo
          (lo que es particular)
```

> Este esquema solo resume la idea de generalización y especialización; todavía no es el diagrama UML.

### Una alternativa que consideré

Las dos humedades usan la misma unidad (%), así que pensé en un concepto intermedio "sensor de humedad" del que salieran ambas. Lo descarté porque lo único que comparten es la unidad: sus rangos y su significado son distintos. Tampoco tiene sentido decir que el sensor de humedad del suelo **es un** sensor de humedad ambiental; son tipos hermanos, no uno especialización del otro. Por eso los tres tipos se especializan directamente del sensor general.

---

## 5. Relaciones entre objetos

### Relaciones "ES UN"

- Un sensor de temperatura **es un** sensor.
- Un sensor de humedad ambiental **es un** sensor.
- Un sensor de humedad del suelo **es un** sensor.

Estas relaciones indican generalización y especialización, por lo que **sí se representarán con herencia**.

### Relaciones "TIENE / UTILIZA UN"

- El invernadero **tiene** varios sensores.
- El invernadero **tiene** un sistema de riego.
- El invernadero **utiliza** la información del sensor de humedad del suelo para decidir si el sistema de riego se activa.

Estas relaciones son de colaboración, **no de herencia**.

### Colaboración entre objetos

| Objeto que necesita algo | De quién | Qué necesita | Para qué |
|---|---|---|---|
| Invernadero | Cada sensor | La interpretación de su medición | Mostrar el estado general |
| Invernadero | Sensor de humedad del suelo | Saber si la humedad del suelo es baja | Decidir si se riega |
| Sistema de riego | Invernadero | La indicación de activarse o desactivarse | Cambiar su estado |
| Invernadero | Sistema de riego | Su estado actual | Mostrarlo en consola |

### ¿Cuáles relaciones se representan con herencia y cuáles no?

| Relación | ¿Herencia? | Motivo |
|---|---|---|
| Sensor de temperatura → Sensor | Sí | Es un sensor |
| Sensor de humedad ambiental → Sensor | Sí | Es un sensor |
| Sensor de humedad del suelo → Sensor | Sí | Es un sensor |
| Invernadero → Sensor | No | El invernadero contiene sensores, no es uno |
| Invernadero → Sistema de riego | No | El invernadero contiene un sistema de riego, no es uno |
| Sistema de riego → Sensor | No | El sistema de riego actúa; no observa (ver la pregunta final) |

### Responsabilidades que no deben duplicarse

- **Interpretar una medición** le corresponde solo al sensor que la produce. Ni el invernadero ni el sistema de riego deben repetir los rangos. Si, por ejemplo, el sistema de riego comparara por su cuenta si la humedad es menor a 30 %, el criterio quedaría escrito en dos lugares y habría que cambiarlo en ambos si se modificara.
- **La información común** (identificación, ubicación, estado y última medición) debe existir una sola vez en el concepto general de sensor, no repetida en cada tipo.
- **El estado del riego** solo lo conserva y modifica el sistema de riego. El invernadero le pide que se active o desactive, pero no cambia ese estado directamente.
- **La decisión de regar** se toma en un solo lugar: el invernadero.

### ¿Por qué `SistemaRiego` no debería ser una subclase de `Sensor`?

Porque sus responsabilidades son diferentes, incluso opuestas:

1. **Un sensor observa; el sistema de riego actúa.** El sensor obtiene información del entorno, y el sistema de riego modifica el entorno suministrando agua. Corresponden a pasos distintos del ciclo observar → evaluar → actuar.
2. **No se cumple la relación "es un".** Un sistema de riego no es un sensor; solamente usa (de forma indirecta) la información que un sensor proporciona.
3. **Heredaría cosas que no tienen sentido para él.** Tendría una "última medición", una unidad y un comportamiento para "interpretar su medición", pero no mide nada, así que no podría cumplirlos de forma coherente.
4. **Compartir algunos datos no justifica la herencia.** Es cierto que ambos tienen una identificación y un estado activo/inactivo, pero esa coincidencia no los convierte en el mismo tipo de cosa. La herencia debe venir de una relación conceptual, no de querer reutilizar un par de atributos.
5. **Rompería el trato común a los sensores.** Si fuera un sensor, podría guardarse en la misma colección que los demás, y al pedirle a cada elemento que interprete su medición, el sistema de riego no tendría nada que interpretar.

La relación correcta es de **colaboración**: el invernadero consulta al sensor de humedad del suelo y, con esa información, le indica al sistema de riego si debe activarse o desactivarse.

---

# Fase 2. Diseño orientado a objetos

## 6. Diseño de clases

A partir de los objetos y responsabilidades de las secciones 2 y 3 propongo **seis clases**. La clase `Main` no forma parte del modelo: solo crea los objetos, simula las mediciones y muestra los resultados.

### 6.1 Propuesta de clases

| Clase | Atributos propuestos | Tipo de dato | Métodos propuestos | Responsabilidad |
|---|---|---|---|---|
| `Sensor` | `id`<br>`ubicacion`<br>`unidad`<br>`activo`<br>`ultimaMedicion` | `String`<br>`String`<br>`String`<br>`boolean`<br>`double` | `Sensor(id, ubicacion, unidad)`<br>`getId()`, `getUbicacion()`, `getUnidad()`<br>`isActivo()`, `getUltimaMedicion()`<br>`activar()`, `desactivar()`<br>`registrarMedicion(valor)`<br>`evaluarMedicion()`<br>`obtenerResumen()` | Guardar y administrar lo que tienen en común todos los sensores. |
| `SensorTemperatura` | `LIMITE_BAJO` (18)<br>`LIMITE_ALTO` (30) | `double` constante<br>`double` constante | `SensorTemperatura(id, ubicacion)`<br>`evaluarMedicion()` *(sobrescrito)*<br>`convertirAFahrenheit()` | Interpretar la temperatura del aire en °C. |
| `SensorHumedad` | `LIMITE_BAJO` (40)<br>`LIMITE_ALTO` (70) | `double` constante<br>`double` constante | `SensorHumedad(id, ubicacion)`<br>`registrarMedicion(valor)` *(sobrescrito)*<br>`evaluarMedicion()` *(sobrescrito)* | Interpretar la humedad ambiental en %. |
| `SensorHumedadSuelo` | `LIMITE_BAJO` (30)<br>`LIMITE_ALTO` (70) | `double` constante<br>`double` constante | `SensorHumedadSuelo(id, ubicacion)`<br>`registrarMedicion(valor)` *(sobrescrito)*<br>`evaluarMedicion()` *(sobrescrito)*<br>`requiereRiego()` | Interpretar la humedad del suelo e indicar si hace falta regar. |
| `SistemaRiego` | `id`<br>`activo` | `String`<br>`boolean` | `SistemaRiego(id)`<br>`getId()`, `isActivo()`<br>`activar()`, `desactivar()`<br>`obtenerEstado()` | Suministrar o detener el agua. No mide ni decide. |
| `Invernadero` | `nombre`<br>`sensores`<br>`sistemaRiego` | `String`<br>`ArrayList<Sensor>`<br>`SistemaRiego` | `Invernadero(nombre, sistemaRiego)`<br>`getNombre()`<br>`agregarSensor(sensor)`<br>`evaluarSensores()`<br>`actualizarRiego()`<br>`mostrarEstado()` | Agrupar los sensores y el sistema de riego, y decidir cuándo regar. |

> `SensorHumedad` representa la **humedad ambiental**. Uso ese nombre porque es el que pide la práctica.

### 6.2 Comportamiento esperado de los métodos principales

| Método | Clase | Qué hará |
|---|---|---|
| `registrarMedicion(valor)` | `Sensor` | Guarda el valor como última medición **solo si el sensor está activo**. Devuelve `true` si se registró y `false` si no. |
| `registrarMedicion(valor)` | `SensorHumedad` y `SensorHumedadSuelo` | Rechaza (devuelve `false`) los valores menores a 0 o mayores a 100. Si el valor es válido, llama a `super.registrarMedicion(valor)` para que la superclase haga el resto. |
| `evaluarMedicion()` | `Sensor` | Versión general: devuelve `"Sin criterio de interpretación"`. Existe para que el método se pueda llamar desde cualquier referencia de tipo `Sensor`. |
| `evaluarMedicion()` | `SensorTemperatura` | menor a 18 → `"Temperatura baja"`; de 18 a 30 → `"Temperatura adecuada"`; mayor a 30 → `"Temperatura alta"`. |
| `evaluarMedicion()` | `SensorHumedad` | menor a 40 → `"Humedad ambiental baja"`; de 40 a 70 → `"Humedad ambiental adecuada"`; mayor a 70 → `"Humedad ambiental alta"`. |
| `evaluarMedicion()` | `SensorHumedadSuelo` | menor a 30 → `"Humedad del suelo baja"`; de 30 a 70 → `"Humedad del suelo adecuada"`; mayor a 70 → `"Humedad del suelo alta"`. |
| `obtenerResumen()` | `Sensor` | Arma un texto con identificación, ubicación, estado, medición con su unidad e interpretación. Si el sensor está inactivo lo indica en lugar de evaluar. Llama a `evaluarMedicion()`, así que cada sensor muestra **su propia** interpretación sin que este método se repita en las subclases. |
| `convertirAFahrenheit()` | `SensorTemperatura` | Devuelve la última medición en °F (`°C × 9/5 + 32`). |
| `requiereRiego()` | `SensorHumedadSuelo` | Devuelve `true` si el sensor está activo y su medición es menor a `LIMITE_BAJO`. Usa la misma constante que `evaluarMedicion()`, así el criterio existe en un solo lugar. |
| `evaluarSensores()` | `Invernadero` | Recorre la lista de sensores y muestra el resumen de cada uno, **sin** usar `if/else` para saber de qué tipo es cada sensor. |
| `actualizarRiego()` | `Invernadero` | Busca en la lista los sensores de humedad del suelo (`instanceof`), los convierte a ese tipo (casting) y les pregunta `requiereRiego()`. Si alguno lo necesita, activa el riego; si no, lo desactiva. |
| `mostrarEstado()` | `Invernadero` | Muestra el nombre del invernadero, la evaluación de los sensores y el estado del riego. |
| `obtenerEstado()` | `SistemaRiego` | Devuelve `"ACTIVO - suministrando agua"` o `"INACTIVO - riego detenido"`. |

Los valores que caen **exactamente en un límite** (18, 30, 40 o 70) los considero "adecuados", porque el documento de contexto dice "entre 18 °C y 30 °C" (y lo mismo para las humedades). En el código esto será: `< LIMITE_BAJO` → baja, `> LIMITE_ALTO` → alta y cualquier otro caso → adecuada.

### 6.3 Encapsulación

**Todos los atributos serán `private`** y todos los métodos de la tabla serán `public`.

| Atributo | Clase | ¿Cómo se consulta desde fuera? | ¿Cómo puede cambiar? | Por qué es `private` |
|---|---|---|---|---|
| `id` | `Sensor` | `getId()` | Solo en el constructor | Identifica al sensor; no debería cambiar una vez creado. |
| `ubicacion` | `Sensor` | `getUbicacion()` | Solo en el constructor | Supongo que un sensor no se cambia de lugar durante la ejecución. |
| `unidad` | `Sensor` | `getUnidad()` | Solo en el constructor, por medio de `super(...)` | La define cada subclase. Si se pudiera cambiar desde fuera, un sensor de temperatura podría terminar diciendo "%". |
| `activo` | `Sensor` | `isActivo()` | `activar()` / `desactivar()` | El estado cambia con acciones con nombre, no asignando un valor cualquiera. |
| `ultimaMedicion` | `Sensor` | `getUltimaMedicion()` | `registrarMedicion(valor)` | Si fuera pública, `sensor.ultimaMedicion = 25;` se saltaría las reglas: guardaría valores en un sensor inactivo o un 150 % de humedad. |
| `LIMITE_BAJO`, `LIMITE_ALTO` | Las tres subclases | No se consultan | Nunca (son `static final`) | Son el criterio interno de cada sensor; nadie fuera de la clase necesita conocerlos. |
| `id` | `SistemaRiego` | `getId()` | Solo en el constructor | Igual que en los sensores. |
| `activo` | `SistemaRiego` | `isActivo()`, `obtenerEstado()` | `activar()` / `desactivar()` | Solo el sistema de riego debe cambiar su propio estado. |
| `nombre` | `Invernadero` | `getNombre()` | Solo en el constructor | — |
| `sensores` | `Invernadero` | No se expone la lista | `agregarSensor(sensor)` | Si la lista fuera pública, `Main` podría vaciarla o agregar `null` sin control. |
| `sistemaRiego` | `Invernadero` | No se expone | Solo en el constructor | Así el riego solo se activa o desactiva por medio de `actualizarRiego()`. |

Decisiones relacionadas:

- **No propongo *setters*** como `setUltimaMedicion()` o `setActivo()`. Cada cambio se hace con un método que tiene sentido en el problema (`registrarMedicion`, `activar`, `desactivar`), y es ahí donde se aplican las reglas.
- Las subclases **tampoco pueden usar directamente** los atributos privados de `Sensor`. Para leer la medición dentro de `evaluarMedicion()` usarán `getUltimaMedicion()`. Me parece correcto: ni siquiera una subclase puede cambiar la medición sin pasar por `registrarMedicion()`.

### 6.4 Constructores

| Clase | Constructor | Información que recibe | Qué inicializa |
|---|---|---|---|
| `Sensor` | `Sensor(String id, String ubicacion, String unidad)` | Identificación, ubicación y unidad | Guarda los tres datos; `activo = true`; `ultimaMedicion = 0.0` |
| `SensorTemperatura` | `SensorTemperatura(String id, String ubicacion)` | Identificación y ubicación | Llama a `super(id, ubicacion, "°C")` |
| `SensorHumedad` | `SensorHumedad(String id, String ubicacion)` | Identificación y ubicación | Llama a `super(id, ubicacion, "%")` |
| `SensorHumedadSuelo` | `SensorHumedadSuelo(String id, String ubicacion)` | Identificación y ubicación | Llama a `super(id, ubicacion, "%")` |
| `SistemaRiego` | `SistemaRiego(String id)` | Identificación | `activo = false` (empieza detenido) |
| `Invernadero` | `Invernadero(String nombre, SistemaRiego sistemaRiego)` | Nombre y el sistema de riego que controlará | Guarda ambos y crea la lista de sensores vacía |

Las subclases **no reciben la unidad** como parámetro: cada una la pasa fija a `super(...)`. Así es imposible crear un `SensorTemperatura` en "%".

---

## 7. Jerarquía de clases

```text
Sensor                   ← superclase
├── SensorTemperatura    ← subclase
├── SensorHumedad        ← subclase
└── SensorHumedadSuelo   ← subclase

SistemaRiego             ← fuera de la jerarquía
Invernadero              ← fuera de la jerarquía (coordina)
```

| Clase | ¿Forma parte de la jerarquía de `Sensor`? | Cómo se relaciona con las demás |
|---|---|---|
| `SensorTemperatura` | Sí, es subclase | **Es un** `Sensor` |
| `SensorHumedad` | Sí, es subclase | **Es un** `Sensor` |
| `SensorHumedadSuelo` | Sí, es subclase | **Es un** `Sensor`; el invernadero usa su método `requiereRiego()` |
| `SistemaRiego` | No | El invernadero **tiene** un sistema de riego y lo controla |
| `Invernadero` | No | **Tiene** varios sensores y un sistema de riego |

Las tres subclases heredan **directamente** de `Sensor`, sin clases intermedias (la razón está en la sección 4, "Una alternativa que consideré").

---

## 8. Diagrama UML inicial

![Diagrama UML inicial](docs/uml-inicial-invernadero.png)

Cómo leer el diagrama:

- `-` significa atributo privado y `+` método público.
- Los atributos **subrayados** son `static`; `{readOnly}` indica que son `final` (constantes).
- En cada clase, el primer método del segundo compartimento es el **constructor**.
- La flecha con **triángulo vacío** representa herencia (la punta señala a la superclase).
- El **rombo vacío** representa agregación: el invernadero tiene sensores y un sistema de riego, pero esos objetos se crean fuera de él (en `Main`) y se le entregan.
- La flecha **punteada «usa»** representa una dependencia: el invernadero usa un método que solo existe en `SensorHumedadSuelo`, por eso necesita `instanceof` y casting.
- Un método que aparece en `Sensor` y se repite en una subclase con la misma firma (`evaluarMedicion()`, `registrarMedicion(valor)`) es un método **sobrescrito**.
- **No existe ninguna línea entre `SistemaRiego` y `Sensor`**: no hay herencia entre ellos y solo se conectan por medio del invernadero.

---

## 9. Justificación del diseño

### 1. ¿Por qué propuse esas clases?

Cada clase sale de un objeto identificado en la sección 2. `Sensor` existe para no repetir lo que comparten los tres sensores. Las tres subclases existen porque cada tipo interpreta su medición de forma diferente. `SistemaRiego` existe porque tiene un estado propio que cambia y acciones propias. `Invernadero` existe para que la decisión de riego no termine dentro de `Main`, de un sensor o del sistema de riego. No creé clases para la medición, la interpretación ni los rangos (las razones están en la sección 2).

### 2. ¿Cuál es la responsabilidad principal de cada una?

| Clase | Responsabilidad principal |
|---|---|
| `Sensor` | Administrar los datos y comportamientos comunes de cualquier sensor. |
| `SensorTemperatura` | Interpretar la temperatura del aire. |
| `SensorHumedad` | Interpretar la humedad ambiental. |
| `SensorHumedadSuelo` | Interpretar la humedad del suelo e indicar si hace falta regar. |
| `SistemaRiego` | Activar o detener el riego. |
| `Invernadero` | Coordinar sensores y riego, y tomar la decisión de regar. |

### 3. ¿Por qué determinados atributos serán `private`?

Todos lo serán, para que el estado de cada objeto solo cambie mediante sus propios métodos y se respeten las reglas: no registrar mediciones en sensores inactivos, no aceptar humedades fuera de 0–100 % y no cambiar la unidad o la identificación después de crear el objeto. El detalle está en la sección 6.3.

### 4. ¿Qué información proporcionaré mediante los constructores?

Lo que un objeto necesita para existir desde el inicio y que no cambiará después: identificación y ubicación para los sensores, la unidad (que cada subclase pasa fija con `super(...)`), la identificación del sistema de riego y, para el invernadero, su nombre y el sistema de riego que controlará. El estado inicial (sensor activo, medición en 0, riego detenido) lo fija el constructor sin pedirlo. El detalle está en la sección 6.4.

### 5. ¿Cuál será la superclase?

`Sensor`.

### 6. ¿Qué clases serán subclases?

`SensorTemperatura`, `SensorHumedad` y `SensorHumedadSuelo`.

### 7. ¿Por qué existe una relación "es un" entre ellas?

Porque todo sensor de temperatura, de humedad ambiental o de humedad del suelo **es** un sensor: tiene identificación, ubicación, estado y medición, y puede registrar e interpretar mediciones. Por eso, en cualquier parte del programa donde se espere un `Sensor` (por ejemplo, la lista del invernadero) se puede usar cualquiera de los tres tipos y todo sigue teniendo sentido. Al revés no se cumple: no todo sensor es un sensor de temperatura.

### 8. ¿Qué información o comportamiento coloqué en la superclase?

- Los cinco atributos comunes: `id`, `ubicacion`, `unidad`, `activo` y `ultimaMedicion`.
- Sus métodos de consulta, `activar()`, `desactivar()` y `registrarMedicion(valor)`, que funcionan igual para todos.
- `obtenerResumen()`, que se escribe una sola vez y sirve para los tres tipos porque internamente llama a `evaluarMedicion()`.
- Una **versión general** de `evaluarMedicion()`. Aunque cada subclase la va a sobrescribir, tiene que existir en `Sensor`: si no, una referencia de tipo `Sensor` no podría llamar a `evaluarMedicion()` y no habría polimorfismo.

Sobre la unidad: el atributo es común (todos los sensores tienen una), pero el **valor** es particular. Por eso el atributo está en la superclase y el valor lo decide cada subclase con `super(...)`.

> Si se pudieran usar clases abstractas, `Sensor` sería abstracta, porque en el invernadero no existe un "sensor genérico". Como esta práctica no las usa, `Sensor` será una clase normal y su `evaluarMedicion()` devolverá un texto por defecto. En el programa nunca crearé un objeto `new Sensor(...)` directamente.

### 9. ¿Qué elementos permanecerán en las subclases?

- Los límites de interpretación (`LIMITE_BAJO` y `LIMITE_ALTO`) y la implementación de `evaluarMedicion()`.
- En las dos humedades, la validación de que el valor esté entre 0 y 100 (`registrarMedicion` sobrescrito).
- `convertirAFahrenheit()` en `SensorTemperatura`: solo tiene sentido para una temperatura.
- `requiereRiego()` en `SensorHumedadSuelo`: preguntarle a un sensor de temperatura si "requiere riego" no tiene sentido, así que no lo puse en `Sensor` solo para evitar el casting.

Consideré poner los límites en la superclase y que `Sensor` hiciera la comparación para todos. Lo descarté porque entonces los tres sensores interpretarían con exactamente el mismo código y sobrescribir `evaluarMedicion()` ya no tendría sentido. Además, un sensor futuro podría no interpretar con dos límites (por ejemplo, uno de luminosidad podría distinguir entre "día" y "noche").

### 10. ¿Por qué `SistemaRiego` no pertenece a la jerarquía de sensores?

Porque no **es un** sensor: el sensor observa y el sistema de riego actúa (la explicación completa está al final de la sección 5). En el diseño esto se ve en que `SistemaRiego` no hereda de nadie, no tiene `ultimaMedicion` ni `evaluarMedicion()`, y en el UML no hay ninguna línea que lo una con `Sensor`. La única conexión es indirecta: el `Invernadero` le pregunta al `SensorHumedadSuelo` si hace falta regar y luego le indica al `SistemaRiego` que se active o se desactive.

### 11. ¿Qué decisiones tomé para evitar duplicar responsabilidades?

- Los atributos comunes y sus métodos están **una sola vez**, en `Sensor`. Las subclases no los vuelven a declarar.
- `obtenerResumen()` se escribe una vez en `Sensor` y sirve para todos gracias a que llama a `evaluarMedicion()`.
- El criterio para saber si la humedad del suelo es baja existe **en un solo lugar**: `requiereRiego()` usa la misma constante `LIMITE_BAJO` que `evaluarMedicion()`. Ni el invernadero ni el sistema de riego repiten el número 30.
- El estado del riego solo lo guarda y modifica `SistemaRiego`.
- La decisión de regar solo se toma en `Invernadero.actualizarRiego()`.

La única repetición que acepté conscientemente es la validación de 0 a 100 %, que aparece en `SensorHumedad` y en `SensorHumedadSuelo`. Es una regla de una línea y, para eliminarla, tendría que crear la clase intermedia que descarté en la sección 4. Me pareció más claro repetir esa línea que forzar una jerarquía que no corresponde al problema.

---

## 10. Aspectos que podrían cambiar durante la implementación

Dejo anotadas las decisiones de las que no estoy completamente seguro, para revisarlas al comparar el UML inicial con el final:

- Usar `ArrayList<Sensor>` o un arreglo `Sensor[]` para la colección del invernadero.
- Si `evaluarMedicion()` también debería tomar en cuenta si el sensor está inactivo, o si basta con que lo haga `obtenerResumen()`.
- Si hace falta distinguir un sensor que todavía no tiene mediciones de uno que midió exactamente 0.
- Si `mostrarEstado()` y `evaluarSensores()` deben seguir siendo métodos separados.

---

# Fase 3. Implementación de la jerarquía y sobrescritura

## 11. Implementación

En esta fase implementé la jerarquía de sensores tal como aparece en el UML inicial:

| Archivo | Contenido |
|---|---|
| `src/Sensor.java` | Superclase con los cinco atributos `private`, constructor, métodos de consulta, `activar()`, `desactivar()`, `registrarMedicion()`, la versión general de `evaluarMedicion()` y `obtenerResumen()`. |
| `src/SensorTemperatura.java` | Límites 18 y 30, constructor con `super(id, ubicacion, "°C")`, `convertirAFahrenheit()` y `evaluarMedicion()` sobrescrito. |
| `src/SensorHumedad.java` | Límites 40 y 70, constructor con `super(id, ubicacion, "%")`, `registrarMedicion()` y `evaluarMedicion()` sobrescritos. |
| `src/SensorHumedadSuelo.java` | Límites 30 y 70, constructor con `super(id, ubicacion, "%")`, `requiereRiego()`, `registrarMedicion()` y `evaluarMedicion()` sobrescritos. |
| `src/Main.java` | Crea un sensor de cada tipo y prueba sus comportamientos. |

`SistemaRiego` e `Invernadero` todavía no están implementados; corresponden a las fases 4 y 5.

### Encapsulación aplicada

Ningún atributo se modifica directamente desde `Main`. Por ejemplo, para cambiar la medición no existe `sensor.ultimaMedicion = 25;` (ni siquiera compilaría, porque el atributo es `private`). La única forma es `registrarMedicion(valor)`, que aplica las reglas:

- Un sensor inactivo no acepta mediciones nuevas.
- Los sensores de humedad no aceptan valores fuera de 0 a 100 %.

Las subclases tampoco tocan los atributos de `Sensor` directamente: dentro de `evaluarMedicion()` leen el valor con `getUltimaMedicion()`.

### Decisiones tomadas al implementar

- **`evaluarMedicion()` no revisa si el sensor está inactivo.** Solo interpreta el número. Quien revisa el estado es `obtenerResumen()`, que muestra "sin evaluación" si el sensor está inactivo. Así la revisión está en un solo lugar y no se repite en las tres subclases. (Esto resuelve uno de los puntos de la sección 10.)
- Los valores que caen justo en un límite se consideran adecuados: en el código, `< LIMITE_BAJO` es bajo, `> LIMITE_ALTO` es alto y el resto es adecuado.
- `Main` usa por ahora **referencias del tipo específico** (`SensorTemperatura temperatura = ...`). El uso de referencias de tipo `Sensor` y la colección polimórfica corresponden a la Fase 4.

Hasta este punto **el código corresponde al UML inicial sin cambios**.

### Resultado de la ejecución

```text
Sistema de invernadero inteligente

=== 1. Sensores creados (constructores, super y métodos heredados) ===
T-01 | Zona norte | unidad: °C | activo: true
H-01 | Zona central | unidad: % | activo: true
S-01 | Zona sur | unidad: % | activo: true

=== 2. Sensor de temperatura: baja, adecuada y alta ===
[T-01] Zona norte | ACTIVO | 15.0 °C -> Temperatura baja
[T-01] Zona norte | ACTIVO | 25.0 °C -> Temperatura adecuada
[T-01] Zona norte | ACTIVO | 34.0 °C -> Temperatura alta

=== 3. Sensor de humedad ambiental: baja, adecuada y alta ===
[H-01] Zona central | ACTIVO | 32.0 % -> Humedad ambiental baja
[H-01] Zona central | ACTIVO | 55.0 % -> Humedad ambiental adecuada
[H-01] Zona central | ACTIVO | 82.0 % -> Humedad ambiental alta

=== 4. Sensor de humedad del suelo: baja, adecuada y alta ===
[S-01] Zona sur | ACTIVO | 18.0 % -> Humedad del suelo baja
[S-01] Zona sur | ACTIVO | 48.0 % -> Humedad del suelo adecuada
[S-01] Zona sur | ACTIVO | 83.0 % -> Humedad del suelo alta

=== 5. Mismo valor (32), distinta interpretación ===
T-01: Temperatura alta
H-01: Humedad ambiental baja
S-01: Humedad del suelo adecuada

=== 6. Reglas al registrar mediciones (encapsulación) ===
H-01 intenta registrar 150 %: false | última medición: 32.0 %
T-01 inactivo intenta registrar 20 °C: false | última medición: 32.0 °C
[T-01] Zona norte | INACTIVO | sin evaluación
T-01 activado de nuevo:
[T-01] Zona norte | ACTIVO | 32.0 °C -> Temperatura alta

=== 7. Comportamientos propios de una subclase ===
T-01: 32.0 °C = 89.6 °F
S-01 con 22.0 %: ¿requiere riego? true
S-01 con 55.0 %: ¿requiere riego? false
```

La sección 5 de la salida es la que mejor muestra la sobrescritura: el mismo método, `evaluarMedicion()`, con el mismo valor (32), da tres respuestas diferentes según el tipo de sensor, igual que en el ejemplo del documento de contexto.

---

## 12. Constructores y `super`

### 1. ¿Los constructores de la superclase se heredan?

**No.** Un constructor lleva el nombre de su clase, así que `SensorTemperatura` no recibe el constructor `Sensor(id, ubicacion, unidad)`; tiene que declarar el suyo. Lo comprobé en mi código: si intento crear

```java
//new SensorTemperatura("T-01", "Zona norte", "°C");
```

el programa no compila, porque `SensorTemperatura` solo tiene un constructor de dos parámetros, aunque `Sensor` tenga uno de tres. Lo que sí se heredan son los métodos públicos, como `getId()` o `activar()`. Los atributos `private` de `Sensor` sí forman parte de cada objeto `SensorTemperatura` (cada uno tiene su propio `id`, su `ubicacion`, etc.), pero la subclase no puede usarlos directamente; los consulta con los métodos públicos.

### 2. Cuando creo un objeto de una subclase, ¿qué constructor se ejecuta primero?

**Primero se ejecuta el de la superclase.** Al hacer `new SensorTemperatura("T-01", "Zona norte")`:

1. Se entra al constructor de `SensorTemperatura`.
2. Su primera instrucción es `super(id, ubicacion, "°C")`, que ejecuta el constructor de `Sensor`. Este guarda `id`, `ubicacion` y `unidad`, y deja `activo = true` y `ultimaMedicion = 0.0`.
3. Al terminar, se regresa al constructor de `SensorTemperatura` y se ejecuta lo que siga después de `super(...)`. En mi caso no hay nada más, porque los límites son constantes `static`.

Java obliga a que `super(...)` sea la primera instrucción: la parte "sensor" del objeto tiene que estar lista antes que la parte "sensor de temperatura". También comprobé que si quito la línea `super(...)`, el programa no compila. Sin ella, Java intenta llamar automáticamente a un constructor `Sensor()` sin parámetros, y mi clase `Sensor` no tiene uno.

### 3. ¿Para qué utilicé `super(...)`?

Para dos cosas:

- **Inicializar la parte del objeto que pertenece a `Sensor`.** Los atributos `id`, `ubicacion`, `unidad`, `activo` y `ultimaMedicion` son `private` en `Sensor`, así que la subclase no puede asignarlos directamente. La única forma es pedirle al constructor de `Sensor` que lo haga.
- **Fijar la unidad de cada tipo de sensor.** Cada subclase pasa su unidad fija: `"°C"` en `SensorTemperatura` y `"%"` en las dos humedades. Por eso quien crea el sensor no tiene que (ni puede) indicar la unidad.

Además usé `super` en un método: en `SensorHumedad` y `SensorHumedadSuelo`, el `registrarMedicion(valor)` sobrescrito revisa que el valor esté entre 0 y 100 y después llama a `super.registrarMedicion(valor)`. Así no repito la regla de "solo registrar si está activo", que ya está en `Sensor`; solo agrego la regla que le corresponde a las humedades.

---

## 13. Sobrescritura

### 1. ¿Qué método sobrescribí?

Principalmente **`evaluarMedicion()`**. Está definido en `Sensor` con una versión general y cada subclase lo sobrescribe con sus propios límites y su propio texto:

| Clase | Resultado de `evaluarMedicion()` |
|---|---|
| `Sensor` | `"Sin criterio de interpretación"` |
| `SensorTemperatura` | `"Temperatura baja / adecuada / alta"` (18 y 30 °C) |
| `SensorHumedad` | `"Humedad ambiental baja / adecuada / alta"` (40 y 70 %) |
| `SensorHumedadSuelo` | `"Humedad del suelo baja / adecuada / alta"` (30 y 70 %) |

También sobrescribí **`registrarMedicion(double valor)`** en `SensorHumedad` y `SensorHumedadSuelo`, para agregar la validación de 0 a 100 %.

### 2. ¿Qué indica `@Override`?

Le indica al compilador que ese método **debe reemplazar** a uno que ya existe en la superclase, y le pide que lo verifique. Si me equivoco en el nombre o en los parámetros, el programa no compila. Lo probé escribiendo `evaluarMedicon()` (sin la "i"): con `@Override` aparece el error *"method does not override or implement a method from a supertype"*. Sin la anotación, Java habría aceptado ese método como uno nuevo, y el sensor de temperatura seguiría usando la versión general de `Sensor` sin que yo me diera cuenta.

### 3. ¿Por qué utilicé el mismo nombre de método en las diferentes subclases?

Porque **es la misma acción**: "interpreta tu medición". Lo que cambia es cómo la hace cada sensor, no lo que se le pide. Usar el mismo nombre permite que el código que usa los sensores les pida lo mismo a todos sin preocuparse por el tipo. En mi programa ya se nota en `obtenerResumen()`: está escrito una sola vez en `Sensor`, llama a `evaluarMedicion()` y aun así cada sensor muestra su propia interpretación (secciones 2 a 4 de la salida).

### 4. ¿Qué ventaja tiene esto frente a crear un método diferente para cada tipo de sensor?

Si hubiera creado `evaluarTemperatura()`, `evaluarHumedadAmbiental()` y `evaluarHumedadSuelo()`:

- `obtenerResumen()` no podría escribirse una sola vez en `Sensor`, porque `Sensor` no sabría a cuál de los tres métodos llamar. Tendría que repetirlo en cada subclase.
- Cualquier código que trabaje con varios sensores tendría que averiguar el tipo de cada uno y elegir el método con una cadena de `if/else`.
- Al agregar un tipo nuevo (por ejemplo, un `SensorLuminosidad`) habría que modificar todos esos `if/else`. Con el mismo nombre, basta con que la clase nueva sobrescriba `evaluarMedicion()`.

Esta es la base del polimorfismo que se trabaja en la Fase 4: poder tratar a todos los sensores como `Sensor` y que cada uno responda a su manera.