import { Button } from "react-bootstrap";
import { LeaveTypes } from "../../services/DocumentsService";
import { fetchDaysoffDocumentPdf } from "../../services/DocumentsService";

export default function SingleLeaveDocument({ leave }) {
  const downloadPdf = (leave) => {
    fetchDaysoffDocumentPdf(leave);
    console.log(leave);
  };

  return (
    <tr>
      <td>{leave.dateIssued}</td>
      <td>{leave.dateFrom}</td>
      <td>{leave.dateTo}</td>
      <td>{LeaveTypes[leave.leaveType]}</td>
      <td>{leave.signed ? "Zaakceptowany" : "Oczekujący"}</td>
      <td style={{ padding: "0" }}>
        <Button
          style={{ width: "100%", borderRadius: "0" }}
          onClick={() => downloadPdf(leave)}
        >
          Pobierz plik PDF
        </Button>
      </td>
    </tr>
  );
}
