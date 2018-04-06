<?php
	/*
	*	Login con comprobaciones
	*/
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$identificador = $_REQUEST['identificador'];
		$password = $_REQUEST['password'];
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::getUsuariosLogin($identificador, $password);

		if($registros[0]['total'])
		{
			$registros = Finanzas::getUsuario($identificador, $password);
			$datos["estado"] = 1;
			$datos["usuario"] = $registros;

			print json_encode($datos);
		}
		else
		{
			$datos["estado"] = 0;
			$datos["mensaje"] = "Ha ocurrido un error";
			print json_encode($datos);
		}
	}
?>