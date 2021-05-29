import { useEffect, useMemo, useState } from "react";
import { useQuery } from "react-query";
import { TableInstance } from "../tables/TableInstance";
import { LoadingComponent } from "../loader/LoadingView";
import {
  fetchEmployeeBonuses,
} from "../../services/EmployeeService";

export default function BonusesView() {
  
  const [tableData, setTableData] = useState(null);

  // const {
  //   isLoading: isLoading2,
  //   error: error2,
  //   refetch: refetch2,
  //   data: budget,
  // } = useQuery("budgetForYear", () => getBudgetForYear(year));

  const {
    isLoading,
    error,
    data: apiResponse,
    isFetching,
    refetch,
  } = useQuery("employeeBonuses", () =>
    fetchEmployeeBonuses()
  );

  useEffect(() => {
    console.log(apiResponse?.data); //debug
    setTableData(apiResponse?.data.bonuses);
  }, [apiResponse]);


  const tableColumns = [
    {
      Header: "Rok",
      accessor: "year",
    },
    {
      Header: "Styczeń",
      accessor: "monthlyBonus[0]",
    },
    {
      Header: "Luty",
      accessor: "monthlyBonus[1]",
    },
    {
      Header: "Marzec",
      accessor: "monthlyBonus[2]",
    },
    {
      Header: "Kwiecień",
      accessor: "monthlyBonus[3]",
    },
    {
      Header: "Maj",
      accessor: "monthlyBonus[4]",
    },
    {
      Header: "Czerwiec",
      accessor: "monthlyBonus[5]",
    },
    {
      Header: "Lipiec",
      accessor: "monthlyBonus[6]",
    },
    {
      Header: "Sierpień",
      accessor: "monthlyBonus[7]",
    },
    {
      Header: "Listopad",
      accessor: "monthlyBonus[10]",
    },
    {
      Header: "Grudzień",
      accessor: "monthlyBonus[11]",
    },
  ];

  if (isLoading || !tableData) {
    return <LoadingComponent />;
  }

  if (error) {
    return <div>Wystąpił błąd podczas ładowania danych.</div>;
  }

  if (!tableData) {
    return <div>Nie znaleziono żadnych danych.</div>;
  }

  return (
    <>
    <div className="container">
    <h1 className="my-3">Twoje premie</h1>
    <br></br>
      <TableInstance
        tableData={tableData || []}
        tableColumns={tableColumns}
        initialState={{
          hiddenColumns: ["employeeId"],
        }}
        footer={false}
      />
    </div>
    </>
  );
};
