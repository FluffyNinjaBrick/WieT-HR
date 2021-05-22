import axios from "axios";
import { useState, useEffect } from "react";
import { useQuery } from "react-query";
import { API_URL } from "../../api/Api";
import BonusPieChart from "../charts/BonusPieChart";
import { fetchBonusBudgetForYear } from "../../services/EmployeeService";
import { LoadingComponent } from "../loader/LoadingView";

export default function BonusBudgetStatisticsChartGenerator({ year }) {
  const [budgetLeft, setBudgetLeft] = useState(0);
  const [budgetSize, setBudgetSize] = useState(0);

  const {
    isLoading,
    error,
    data: apiResponse,
    isFetching,
    refetch,
  } = useQuery("bonusBudgetStatistics2", () => fetchBonusBudgetForYear(year));

  useEffect(() => {
    setBudgetLeft(apiResponse?.data?.budgetLeft);
    setBudgetSize(apiResponse?.data?.budgetSize);
    console.log(budgetLeft + "   " + budgetSize);
  }, [apiResponse]);

  useEffect(() => {
    refetch();
  }, [year]);

  const chartData = [
    {
      fetchedStatus: "budgetLeft",
      id: "Pozostały budżet premiowy",
      label: "Pozostały budżet premiowy",
      value: 0,
    },
    {
      fetchedStatus: "budgetSize",
      id: "Wykorzystany budżet premiowy",
      label: "Wykorzystany budżet premiowy",
      value: 0,
    },
  ];

  if (isLoading) return <LoadingComponent />;

  if (error) return "An error has occurred: " + error.message;

  chartData[0].value = budgetLeft;
  chartData[1].value = budgetSize - budgetLeft;

  return <BonusPieChart data={chartData} />;
}
