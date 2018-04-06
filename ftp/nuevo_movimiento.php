<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$idUsuario = $_REQUEST['idU'];
		$idCategoria = $_REQUEST['idC'];
		$monto = $_REQUEST['mon'];
		$idCuenta = $_REQUEST['idCu'];
		$tipo = $_REQUEST['tip'];
		$fecha = $_REQUEST['date'];
		$concepto = $_REQUEST['concepto'];
		$body = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::nuevoMovimiento($idUsuario, $idCategoria, $monto, $idCuenta, $tipo, $fecha, $concepto);

		if($registros)
		{
			$datos["estado"] = 1;
			$datos["mensaje"] = "Movimiento Registrado";
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