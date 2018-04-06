<?php
	/*
	*	Obtiene los movimientos de los ultimos 30 dias
	*/
	require 'Finanzas.php';

	$idUsuario = $_REQUEST['idU'];
	print "khe";

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::getmovsemana($idUsuario);

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