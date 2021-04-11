import EmployeeCreateForm from "./EmployeeCreateForm";

export default function EmployeeCreateView() {
  return (
    <div className="container">
      <h1 className="mt-5">Dodaj pracowika</h1>
      <hr />
      <EmployeeCreateForm />
    </div>
  );
}
