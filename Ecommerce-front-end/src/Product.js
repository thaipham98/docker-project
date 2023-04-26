import React from "react";
import { Card, Collapse, Button } from "antd";
import useAddToCart from "./hooks/useAddToCart";

const { Meta } = Card;
function Product({ id, name, detail, price }) {
  const { mutate } = useAddToCart();

  return (
    <Card hoverable style={{ width: 250, margin: 16 }}>
      <Meta title={name} />
      <div style={{ marginTop: 12, marginBottom: 12 }}>
          <p>{detail}</p>
          <p>${price}</p>
      </div>
      <Button onClick={() => mutate(id)} type="primary">
        Add to cart
      </Button>
    </Card>
  );
}

export default Product;
