import SingleSubordinateEmployeeBrief from "./SingleSubordinateEmployeeBrief";

export default function SubordinateEmployeeList({
  employees,
  managed,
  onChange,
}) {
  const listOfEmployees = employees.map((employee) => {
    return (
      <SingleSubordinateEmployeeBrief
        key={employee.id}
        employee={employee}
        onChange={onChange}
        managed={managed}
      />
    );
  });

  return !employees.length && managed ? (
    <div>Nie wybrano żadnych podwładnych.</div>
  ) : !employees.length && !managed ? (
    <div>Nie znaleziono pracowników.</div>
  ) : (
    <div>{listOfEmployees}</div>
  );
}
