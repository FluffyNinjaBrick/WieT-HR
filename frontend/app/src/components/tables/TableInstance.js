import { TableLayout } from "./TableLayout";
import { useTable, useSortBy, useGlobalFilter } from "react-table";
import { useMemo } from "react";

export const TableInstance = ({ tableData, tableColumns }) => {
  const [columns, data] = useMemo(() => {
    return [tableColumns, tableData];
  }, [tableData, tableColumns]);

  const tableInstance = useTable({ columns, data }, useGlobalFilter, useSortBy);

  return <TableLayout {...tableInstance} />;
};
