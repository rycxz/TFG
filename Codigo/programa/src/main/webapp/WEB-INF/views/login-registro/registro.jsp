<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro</title>
</head>
<body>

    <h1>Registro de nuevo usuario</h1>
    <form action="/auth/register" method="post">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required><br>

        <label for="email">Correo:</label>
        <input type="email" id="email" name="email" required><br>

        <label for="numero">Número:</label>
        <input type="text" id="numero" name="numero" required><br>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required><br>

        <label for="fechaNacimiento">Fecha Nacimiento:</label>
        <input type="date" id="fechaNacimiento" name="fechaNacimiento" required><br>

        <button type="submit">Registrarse</button>
    </form>
</body>
</html>
