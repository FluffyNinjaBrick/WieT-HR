import { useState, useEffect } from "react";
import { useQuery } from "react-query";
import { fetchBonusBudgetForYear } from "../../services/EmployeeService";
import BonusBudgetStatisticsChartGenerator from "./BonusBudgetStatisticsChartGenerator";
import { BonusBudgetStatisticsContainer } from "../../styled";
import { PieChartContainer } from "../../styled";

export default function BonusBudgetStatistics() {
  const [budgetLeft, setBudgetLeft] = useState(0);
  const [budgetSize, setBudgetSize] = useState(0);
  const [budgetUsed, setBudgetUsed] = useState(0);

  const {
    isLoading,
    error,
    data: apiResponse,
    isFetching,
    refetch,
  } = useQuery("bonusBudgetStatistics", () =>
    fetchBonusBudgetForYear(new Date().getFullYear())
  );

  useEffect(() => {
    setBudgetLeft(apiResponse?.data?.budgetLeft);
    setBudgetSize(apiResponse?.data?.budgetSize);
    setBudgetUsed(
      apiResponse?.data?.budgetSize - apiResponse?.data?.budgetLeft
    );
    console.log(budgetLeft + "   " + budgetSize);
  }, [apiResponse]);

  return (
    <BonusBudgetStatisticsContainer>
      <div>
        <h4 className="mb-3">Budżet premiowy: {budgetSize}</h4>
        <h4 className="mb-3">Wykorzystany budżet premiowy: {budgetUsed}</h4>
        <h4 className="mt-3">Pozostały budżet premiowy: {budgetLeft}</h4>
      </div>
      <PieChartContainer>
        <BonusBudgetStatisticsChartGenerator />
      </PieChartContainer>
    </BonusBudgetStatisticsContainer>
  );
}
