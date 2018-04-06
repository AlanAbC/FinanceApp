<?php
	/*
	*	Login con comprobaciones
	*/
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$nombre = $_REQUEST['nom'];
		$username = $_REQUEST['use'];
		$password = $_REQUEST['pas'];
		$correo = $_REQUEST['cor'];
		$sexo = $_REQUEST['sex'];
		$fnacimiento = $_REQUEST['fna'];
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::getExistsUser($username, $correo);

		if($registros[0]['total'] == 0)
		{

			$registros = Finanzas::getExistsMail($correo);
			if($registros[0]['total'] == 0)
			{
				$retorno = Finanzas::insertar_usuario($nombre, $username, $password, $correo, $sexo, $fnacimiento);
				if($retorno)
				{
					$datos["estado"] = 1;
					$datos["mensaje"] = "Registro realizado con exito";
					print json_encode($datos);
					Finanzas::crearCuentaUsuarioNuevo($username);
					Finanzas::crearCategoriaUsuarioNuevo($username);
				}
				else
				{
					$datos["estado"] = 0;
					$datos["mensaje"] = "Ha ocurrido un error";
					print json_encode($datos);
				}
			}
			else
			{
				$datos["estado"] = 0;
				$datos["mensaje"] = "El correo ya existe";
				print json_encode($datos);
				}
		}
		else
		{
			$datos["estado"] = 0;
			$datos["mensaje"] = "El usuario ya existe";
			print json_encode($datos);
		}
	}
?>