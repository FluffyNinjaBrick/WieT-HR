import EmployeeEditForm from "./EmployeeEditForm";

export default function EmployeeCreateView() {
  return (
    <div className="container">
      <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
        <h1 className="mt-5">Dodaj pracownika</h1>
        <hr />
      </div>
      <EmployeeEditForm />
    </div>
  );
}
