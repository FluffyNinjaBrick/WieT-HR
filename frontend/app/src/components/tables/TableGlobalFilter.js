import { useState } from "react";
import { useAsyncDebounce } from "react-table";
import { TableSearchInput } from "../../styled";

export default function TableGlobalFilter({ globalFilter, setGlobalFilter }) {
  const [value, setValue] = useState(globalFilter);
  const onChange = useAsyncDebounce((value) => {
    setGlobalFilter(value || undefined);
  }, 200);

  return (
    <span>
      <TableSearchInput
        value={value || ""}
        onChange={(e) => {
          setValue(e.target.value);
          onChange(e.target.value);
        }}
        placeholder={`Szukaj...`}
      />
    </span>
  );
}
