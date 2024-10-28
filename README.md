# Proyecto-PPM
# Proyecto

Este es un proyecto de Android que utiliza Jetpack Compose para crear una aplicación con varias pantallas, incluyendo inicio de sesión, registro, recuperación de contraseña, perfil, y más.

## Características

- Pantalla de inicio de sesión
- Pantalla de registro
- Pantalla de recuperación de contraseña
- Pantalla de perfil
- Navegación entre pantallas
- Uso de Jetpack Compose para la interfaz de usuario

## Requisitos

- Android Studio Koala | 2024.1.1 Patch 1
- Kotlin 1.5+
- Gradle 7.0+
- 
## Librerías Utilizadas

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Para la interfaz de usuario declarativa.
- [Firebase](https://firebase.google.com/) - Para autenticación y base de datos en tiempo real.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Para la persistencia de datos local.
- [Serializable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/-serializable/) - Para la serialización de datos.

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/jlope384/Proyecto-PPM.git
    ```

2. Abre el proyecto en Android Studio.

3. Sincroniza el proyecto con Gradle.

4. Ejecuta la aplicación en un emulador o dispositivo físico.

## Uso

### Pantalla de Inicio de Sesión

- Ingresa tu nombre de usuario y contraseña para iniciar sesión.
- Si olvidaste tu contraseña, haz clic en "¿Olvidaste tu contraseña?" para ir a la pantalla de recuperación de contraseña.
- Si no tienes una cuenta, haz clic en "Empezando? Crea tu usuario" para ir a la pantalla de registro.

### Pantalla de Registro

- Ingresa tu correo electrónico, nombre de usuario y contraseña para crear una nueva cuenta.
- Haz clic en "Registrarme" para completar el registro.

### Pantalla de Recuperación de Contraseña

- Ingresa tu correo electrónico asociado a tu cuenta.
- Haz clic en "Reiniciar Contraseña" para recibir instrucciones de recuperación.

### Pantalla de Perfil

- Visualiza y edita tu información de perfil, incluyendo correo electrónico, nombre, apellido y contraseña.

## Contribución

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-caracteristica`).
3. Realiza tus cambios y haz commit (`git commit -am 'Añadir nueva característica'`).
4. Sube tus cambios (`git push origin feature/nueva-caracteristica`).
5. Abre un Pull Request.

