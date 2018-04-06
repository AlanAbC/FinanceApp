<?php
	/*
	*	Obtiene los usuarios registrados
	*/
	require 'Finanzas.php';

	$usuario = $_REQUEST['usuario'];

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::getCategorias($usuario);

		if($registros)
		{
			$datos["estado"] = 1;
			$datos["registros"] = $registros;

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