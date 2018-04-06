 <?php
/**
 * Elimina un movimiento de la base de datos
 */

require 'Finanzas.php';

    $id = $_REQUEST['id'];  

    if(isset($id) && !empty($id)){
        $retorno = Finanzas::eliminar_movimiento($id);

        if($retorno)
        {
            $data["estado"] = 1;
            $data["mensaje"] = "Movimiento eliminado";
            print json_encode($data);
        }
        else
        {
            $data["estado"] = 0;
            $data["mensaje"] = "Ha ocurrido un error";
            print json_encode($data);
        }
        
    }else{
        $data["estado"] = 0;
        $data["mensaje"] = "No se ha enviado ID";
        print json_encode($data);
    }

?>