import AddBonusForm from "./AddBonusForm";

export default function AddBonusView({
  onHide,
  employeeId,
  employeeName,
  year,
  bonusBudgetId,
}) {
  return (
    <div>
      <p>Pracownik: {employeeName}</p>
      <p>Rok: {year}</p>
      <AddBonusForm
        onHide={onHide}
        employeeId={employeeId}
        employeeName={employeeName}
        year={year}
        bonusBudgetId={bonusBudgetId}
      />
    </div>
  );
}
