<!DOCTYPE html>
<html>
	<head>
		<title>Log In</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="estilo.css">
	</head>
	<body >
		<div id="container">

			<img src="imagens/perfil.png">

			<form action="Includes/validarUtilizador_inc.php" method="POST">
				<div>
					<input class="email" id="email" type="text" name="email"  placeholder="Digite o seu email">

					<div class="shadow" id="shadowMail"></div>

				</div>

				<div>
					<input class="senha" type="password" name="senha" id="senha" placeholder="Digite a sua senha">

					<div class="shadow" id="shadowPass"></div>
				</div>

				<?php

				if(isset($_GET['login']) && $_GET['login'] == 'insucess'){ ?>

				
					<div id="text-danger">
						<h1>Usuário ou senha inválido(s)</h1>
					</div>
				

				<?php } ?>

				<div>
					<input class="submit" type="submit" value="LOGIN" >
				</div>
			</form>

		</div>

		<script type="text/javascript" src="./index.js"></script>

	</body>
</html>