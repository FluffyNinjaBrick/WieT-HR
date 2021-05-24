import { useEffect, useState, useMemo } from "react";
import { TableInstance } from "../tables/TableInstance";
import { LoadingComponent } from "../loader/LoadingView";
import { getSalariesForYear } from "../../services/EmployeeService";

export default function TableSalaries(props) {
  const [tableData, setTableData] = useState([]);

  useEffect(() => {
    const fetchSalaries = async () => {
      const year = props.year;
      let salariesResponse = await getSalariesForYear(year);
      setTableData(salariesResponse?.data.monthlySumPerEmployee);
    };
    fetchSalaries();
  }, [props.year]);

  const tableColumns = [
    {
      Header: "Id",
      accessor: "employeeId",
      Footer: "",
    },
    {
      Header: "Imię i nazwisko",
      accessor: "employeeName",
      Footer: "SUMA",
    },
    {
      Header: "Styczeń",
      accessor: "sum",
      accessor: "monthlySum[0]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[0]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Luty",
      accessor: "monthlySum[1]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[1]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Marzec",
      accessor: "monthlySum[2]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[2]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Kwiecień",
      accessor: "monthlySum[3]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[3]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Maj",
      accessor: "monthlySum[4]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[4]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Czerwiec",
      accessor: "monthlySum[5]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[5]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Lipiec",
      accessor: "monthlySum[6]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[6]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Sierpień",
      accessor: "monthlySum[7]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[7]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Wrzesień",
      accessor: "monthlySum[8]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[8]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Październik",
      accessor: "monthlySum[9]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[9]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Listopad",
      accessor: "monthlySum[10]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[10]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Grudzień",
      accessor: "monthlySum[11]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["monthlySum[11]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "SUMA",
      accessor: "sum",
    },
  ];

  if (tableData === []) return <LoadingComponent />;

  return (
    <>
      <TableInstance
        tableData={tableData || []}
        tableColumns={tableColumns}
        initialState={{
          hiddenColumns: ["employeeId"],
        }}
      />
    </>
  );
}
