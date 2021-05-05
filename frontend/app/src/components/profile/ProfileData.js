export default function ProfileData({ title, value }) {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        margin: "1.2em 0",
      }}
    >
      <p style={{ fontSize: "0.9em", fontWeight: "bold", margin: "0" }}>
        {title}
      </p>
      <p style={{ fontSize: "1.2em", margin: "0" }}>{value}</p>
    </div>
  );
}
