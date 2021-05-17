import { Button, Modal } from "react-bootstrap";
import { fetchCurrentEmployee } from "../../services/EmployeeService";
import { signLeave, signDelegation } from "../../services/SignDocumentsService";
import { useEffect, useState } from "react";
import {LeaveTypes} from "../../services/DocumentsService";

export default function RequestSignModal(props){

    const [signed, setSigned] = useState(false);

    const DocumentType = {
      leave: "urlop",
      delegation: "delegację",
    };


    function delayHide(onHide, time) {
      return new Promise(function() { 
          setTimeout(function(){onHide()}, time)
      });
    }

    function signDocument(document, type, onHide) {

      if(type=="leave"){
        signLeave(document);
        setSigned(true)
        delayHide(onHide, 1000).then(() => setSigned(false));


      }
      else if(type=="delegation"){
        signDelegation(document);
        setSigned(true);
        delayHide(onHide, 1000).then(() => setSigned(false));
      }
    }

    return(

      <Modal
        {...props}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            Wniosek o {DocumentType[props.type.toLowerCase()]} {typeof props.leave.leaveType !== "undefined" ?  LeaveTypes[props.leave.leaveType].toLowerCase() : ""}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {!signed ? (
          <div>          
            <h4>{props.leave.nameAtSigning}</h4>
            <p>
              Czas trwania nieobecności:
              Od {props.leave.dateFrom} do {props.leave.dateTo}
            </p>
          </div>) : 
          (
          <div>          
            <h4 style={{ color:"green" }}>Zaakceptowano wniosek</h4>
          </div>
          )}

        </Modal.Body>
        <Modal.Footer>
        {!signed ? (
          <div>          
            <Button className="btn btn-secondary mr-2" onClick={props.onHide}>Anuluj</Button>
            <Button className="btn btn-success" onClick={()=>signDocument(props.leave, props.type, props.onHide)} >Zaakceptuj</Button>
          </div>) : 
          (
          <div>          
          </div>
          )}
          </Modal.Footer>
      </Modal>
    );
}

