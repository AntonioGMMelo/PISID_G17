<!DOCTYPE html>
<html>
	<head>
		<title>Criar Utilizador</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel = "stylesheet" type="text/css" href="../Investigador/css/alterarParametrosCultura.css">
		<link rel="stylesheet" type="text/css" href="./css/criarUtilizador.css">
</head>
	<body >

		<h2>Criar Utilizador</h2>
		<div id="container" >

			

			<form action="../Includes/registarUtilizador_inc.php" method="POST">

				<fieldset>

					<legend>Cadastrar utilizador</legend>

					<div class="parametros">

					<div>
						<label for="nome">Nome do Utilizador:</label>
						<input  type="text" name="nome" id="nome" required / >
					</div>

			
					<div>
						<label for="email">Email:</label>
						<input  type="text" name="email" id="email" required / >
					</div>

					
					<div class="flex">
						<label for="tipo">Tipo de Utilizador: </label>
						<select name="tipo" id="tipo">

						    <option> Investigador </option>
						    <option> administradorAplicacao </option>
						    
					    
						</select>
					
					</div>

					
 



					<div>
						<label for="senha">Senha do Utilizador:</label>
						<input  type="password" name="senha" id="senha" required />
					</div>
					
				</div>

				</fieldset>

				<?php

				if(isset($_GET['signup']) && $_GET['signup'] == 'insucess'){ ?>

				
					<div id= "error"class="text-danger">
						Usuário já existe
					</div>
				

				<?php } ?>



				<div class ="flex">
					<button  type="submit" name="submit" >Registar</button>
				</div>
			</form>


		</div>


				<?php

				if(isset($_GET['signup']) && $_GET['signup'] == 'sucess'){ ?>

				
					<div id = "succes" class="text-danger">
						Usuário registado com exito!
					</div>
				

				<?php } ?>

				
	</body>
</html>