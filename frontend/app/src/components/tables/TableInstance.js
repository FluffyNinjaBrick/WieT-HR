import { TableLayout } from "./TableLayout";
import { useTable, useSortBy, useGlobalFilter } from "react-table";
import { useMemo } from "react";

export const TableInstance = ({ tableData, tableColumns, initialState }) => {
  const [columns, data] = useMemo(() => {
    return [tableColumns, tableData];
  }, [tableData, tableColumns]);

  const tableInstance = useTable(
    {
      columns,
      data,
      initialState: initialState,
    },
    useGlobalFilter,
    useSortBy
  );

  return <TableLayout {...tableInstance} />;
};
