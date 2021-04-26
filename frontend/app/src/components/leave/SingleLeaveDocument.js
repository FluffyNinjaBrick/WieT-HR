import {LeaveTypes} from "../../services/DocumentsService";

export default function SingleEmployeeRecord({ leave }) {
  
    return (
        <tr>
        <td>{leave.dateFrom}</td>
        <td>{leave.dateTo}</td>
        <td>{LeaveTypes[leave.leaveType]}</td>
        <td>{leave.signed ? "Zaakceptowany" : "Oczekujący"}</td>
        </tr>
    );
}
