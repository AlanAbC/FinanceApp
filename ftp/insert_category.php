<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$idUser = $_REQUEST['idUser'];
		$name = $_REQUEST['name'];
		$description = $_REQUEST['description'];
		$body = json_decode(file_get_contents("php://input"), true);
		if(isset($idUser) && !empty($idUser) && isset($name) && !empty($name) && isset($description) && !empty($description))
		{
			$reg = Finanzas::getExistsCat($idUser, $name);

			if($reg[0]['total'] == 0)
			{
				$reg = Finanzas::nuevaCategoria($idUser, $name, $description);
				if($reg)
				{
					$datos["estado"] = 1;
					$datos["mensaje"] = "Categoria Registrada";
					print json_encode($datos);
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
				$datos["mensaje"] = "La categoria ya existe";
				print json_encode($datos);
			}
		}
		else
		{
			$datos["estado"] = 0;
			$datos["mensaje"] = "No se recibieron datos";
			print json_encode($datos);
		}
	}
?>