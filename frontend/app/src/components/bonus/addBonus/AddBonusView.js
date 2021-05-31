import AddBonusForm from "./AddBonusForm";

export default function AddBonusView({
  onHide,
  employeeId,
  employeeName,
  year,
  bonusBudgetId,
  budgetLeft,
  update,
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
        budgetLeft={budgetLeft}
        update={update}
      />
    </div>
  );
}
