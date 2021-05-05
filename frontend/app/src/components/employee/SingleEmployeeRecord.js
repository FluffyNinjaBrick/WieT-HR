import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";

export default function SingleEmployeeRecord({ employee }) {
  return (
    <tr>
      <td>{employee.firstName}</td>
      <td>{employee.lastName}</td>
      <td>{employee.email}</td>
      <td>{employee.address}</td>
      <td>{employee.phone}</td>
      <td>{employee.status}</td>
      <td className="p-0">
        <Link
          to={{
            pathname: "/employees/edit/" + employee.id,
            state: {
              employee: employee,
            },
          }}
        >
          <Button style={{ width: "100%", borderRadius: "0" }}>Edytuj</Button>
        </Link>
      </td>
    </tr>
  );
}
