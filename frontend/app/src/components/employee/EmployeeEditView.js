import EmployeeEditForm from "./EmployeeEditForm";
import { useParams } from "react-router";

export default function EmployeeEditView() {
  const { id } = useParams();

  return (
    <div className="container">
      <h1 className="mt-5">Edytuj pracownika</h1>
      <hr />
      <EmployeeEditForm employeeId={id} />
    </div>
  );
}
