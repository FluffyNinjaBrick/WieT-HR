import { Button } from "react-bootstrap";
import { LeaveTypes } from "../../services/DocumentsService";
import { fetchDaysoffDocumentPdf } from "../../services/DocumentsService";

export default function SingleLeaveDocument({ leave }) {
    
    const downloadPdf = (leave) => {
        fetchDaysoffDocumentPdf(leave);
    }

    return (
        <tr>
        <td>{leave.dateFrom}</td>
        <td>{leave.dateTo}</td>
        <td>{LeaveTypes[leave.leaveType]}</td>
        <td>{leave.signed ? "Zaakceptowany" : "Oczekujący"}</td>
        <td><Button onClick={() => downloadPdf(leave)}>Pobierz plik PDF</Button></td>
        </tr>
    );
}
