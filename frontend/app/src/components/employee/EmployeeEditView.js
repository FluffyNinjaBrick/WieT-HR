import { useParams } from "react-router";

export default function EmployeeEditView() {
  const { id } = useParams();

  return <div>Employee {id}</div>;
}
