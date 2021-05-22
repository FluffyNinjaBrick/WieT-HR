import React from "react";
import { TableContainer } from "../../styled";
import {
  faSort,
  faSortDown,
  faSortUp,
} from "@fortawesome/free-solid-svg-icons";
import TableGlobalFilter from "./TableGlobalFilter";
import { Icon, StyledTable } from "../../styled";

export const TableLayout = ({
  getTableProps,
  getTableBodyProps,
  headerGroups,
  footerGroups,
  rows,
  prepareRow,
  state,
  visibleColumns,
  preGlobalFilteredRows,
  setGlobalFilter,
}) => {
  return (
    <TableContainer>
      <StyledTable {...getTableProps()}>
        <thead>
          <tr>
            <th
              colSpan={visibleColumns.length}
              style={{
                textAlign: "left",
              }}
            >
              <TableGlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
              />
            </th>
          </tr>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column) => (
                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                  {column.render("Header")}
                  <span>
                    {column.isSorted ? (
                      column.isSortedDesc ? (
                        <Icon icon={faSortDown} />
                      ) : (
                        <Icon icon={faSortUp} />
                      )
                    ) : (
                      <Icon icon={faSort} />
                    )}
                  </span>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {rows.map((row, i) => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map((cell) => {
                  return (
                    <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
        <tfoot>
          {footerGroups.map((group) => (
            <tr {...group.getFooterGroupProps()}>
              {group.headers.map((column) => (
                <td {...column.getFooterProps()}>{column.render("Footer")}</td>
              ))}
            </tr>
          ))}
        </tfoot>
      </StyledTable>
    </TableContainer>
  );
};
