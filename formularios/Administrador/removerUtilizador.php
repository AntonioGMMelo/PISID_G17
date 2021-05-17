<!DOCTYPE html>
<html>
	<head>
		<title>Remover Utilizador</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel = "stylesheet" type="text/css" href="../Investigador/css/alterarParametrosCultura.css">
		<link rel="stylesheet" type="text/css" href="./css/criarUtilizador.css">	</head>
	<body >

		<h2>Remover Utilizador</h2>
		<div id="container" >

			

			<form action="../Includes/removerUtilizador_inc.php" method="POST">
				
				<fieldset>
				<div class="parametros">
					<div>
						<label for="email">Email:</label>
						<input  type="text" name="email" id="email" placeholder="Mail do Utilizador" >
					</div>
					</div>
				</fieldset>

				<?php

				if(isset($_GET['removed']) && $_GET['removed'] == 'insucess'){ ?>

				
					<div id = "error"class="text-danger">
						Usuário não existe
					</div>
				

				<?php } ?>

				<div>
					<button  type="submit" name="submit" >Remover</button>
				</div>
			</form>

		</div>


		<?php

				if(isset($_GET['removed']) && $_GET['removed'] == 'sucess'){ ?>

				
					<div id= "succes" class="text-danger">
						Usuário removido com exito!
					</div>
				

				<?php } ?>

	</body>
</html>