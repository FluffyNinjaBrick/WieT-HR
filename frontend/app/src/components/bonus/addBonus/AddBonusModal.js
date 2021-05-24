import { Modal } from "react-bootstrap";
import AddBonusView from "./AddBonusView";

export default function AddBonusModal({
  onHide,
  employeeId,
  employeeName,
  year,
  bonusBudgetId,
  ...props
}) {
  return (
    <Modal
      {...props}
      size="md"
      aria-labelledby="contained-modal-title-vcenter"
      centered
      onHide={onHide}
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          Przydzielanie premii
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <AddBonusView
          employeeId={employeeId}
          employeeName={employeeName}
          onHide={onHide}
          year={year}
          bonusBudgetId={bonusBudgetId}
        />
      </Modal.Body>
    </Modal>
  );
}
