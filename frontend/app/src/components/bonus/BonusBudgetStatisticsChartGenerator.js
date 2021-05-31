import BonusPieChart from "../charts/BonusPieChart";

export default function BonusBudgetStatisticsChartGenerator({
  budgetLeft,
  budgetUsed,
}) {
  const chartData = [
    {
      fetchedStatus: "budgetLeft",
      id: "Pozostały budżet premiowy",
      label: "Pozostały budżet premiowy",
      value: budgetLeft,
    },
    {
      fetchedStatus: "budgetSize",
      id: "Wykorzystany budżet premiowy",
      label: "Wykorzystany budżet premiowy",
      value: budgetUsed,
    },
  ];

  return <BonusPieChart data={chartData} />;
}
