<?php
	/*
	*	Obtiene los movimientos de los ultimos 30 dias filtrados por categoría
	*/
	require 'Finanzas.php';

	$idUsuario = $_REQUEST['idU'];
	$idCategoria = $_REQUEST['idC'];

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::getmovcatsemana($idUsuario, $idCategoria);

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