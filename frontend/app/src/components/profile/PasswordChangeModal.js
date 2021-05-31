import { Modal } from "react-bootstrap";
import PasswordChangeForm from "./PasswordChangeForm";

export default function PasswordChangeModal({ onHide, ...props }) {
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
          Zmiana hasła
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <PasswordChangeForm onHide={onHide} />
      </Modal.Body>
    </Modal>
  );
}
