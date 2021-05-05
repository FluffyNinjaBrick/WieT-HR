import { Button } from "react-bootstrap";
import { fetchDelegationDocumentPdf } from "../../services/DocumentsService";

export default function SingleDelegationDocument({ delegation }) {
  const downloadPdf = (delegation) => {
    fetchDelegationDocumentPdf(delegation);
  };

  return (
    <tr>
      <td>{delegation.dateIssued}</td>
      <td>{delegation.dateFrom}</td>
      <td>{delegation.dateTo}</td>
      <td>{delegation.destination}</td>
      <td>{delegation.signed ? "Zaakceptowany" : "Oczekujący"}</td>
      <td className="p-0">
        <Button
          style={{ width: "100%", borderRadius: "0" }}
          onClick={() => downloadPdf(delegation)}
        >
          Pobierz plik PDF
        </Button>
      </td>
    </tr>
  );
}
