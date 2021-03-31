import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";

export default function SingleEmployeeRecord({ employee }) {
  return (
    <tr>
      <td>{employee.id}</td>
      <td>{employee.first_name}</td>
      <td>{employee.last_name}</td>
      <td>{employee.email}</td>
      <td>{employee.address}</td>
      <td>{employee.phone}</td>
      <td>{employee.status}</td>
      <td className="d-flex justify-content-center">
        <Link to={"/employees/edit/" + employee.id}>
          <Button>Edytuj</Button>
        </Link>
      </td>
    </tr>
  );
}
