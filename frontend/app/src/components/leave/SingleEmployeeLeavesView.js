import { ProgressBar } from "react-bootstrap";
import { Link } from "react-router-dom";

//TODO przeniesc yearsDaysOff jako zmienna bardziej globalna xd
export default function SingleEmployeeLeavesView({ employee }) {
    var yearsDaysOff = 10;
    var daysOffLeft = employee.thisYearDaysOff;
    var colorVariant;

    switch(parseInt(daysOffLeft * 3 /yearsDaysOff)){
        case 0:
            colorVariant = "danger";
            break
        case 1:
            colorVariant = "warning";
            break
        case 2:
            colorVariant = "info";
            break
        default:
            colorVariant = "info";
            break;   
    }

  return (
    <tr>
      <td>{employee.id}</td>
      <td>{employee.firstName}</td>
      <td>{employee.lastName}</td>
      <td>{employee.email}</td>
      <td>{employee.address}</td>
      <td>{employee.phone}</td>
      <td>
        <ProgressBar className="my-1" now={daysOffLeft * 10} label={daysOffLeft + "/" + yearsDaysOff} variant={colorVariant}/>
      </td>
    </tr>
  );
}
