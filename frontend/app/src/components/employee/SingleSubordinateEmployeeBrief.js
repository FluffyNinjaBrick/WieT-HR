import { Button } from "react-bootstrap";

export default function SingleSubordinateEmployeeBrief({
  employee,
  onChange,
  managed,
}) {
  return (
    <div className="d-flex justify-content-start align-items-center p-1">
      {employee.firstName} {employee.lastName}
      {managed === true ? (
        <Button
          className="ml-auto"
          variant="danger"
          onClick={() => onChange(employee)}
        >
          Usuń
        </Button>
      ) : (
        <Button
          className="ml-auto"
          variant="success"
          onClick={() => onChange(employee)}
        >
          Dodaj
        </Button>
      )}
    </div>
  );
}
