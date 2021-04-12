import EmployeeEditForm from "./EmployeeEditForm";

export default function EmployeeCreateView() {
  return (
    <div className="container">
      <h1 className="mt-5">Dodaj pracownika</h1>
      <hr />
      <EmployeeEditForm />
    </div>
  );
}
