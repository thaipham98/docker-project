import React, {useEffect} from "react";
import { Card, Row, Button } from "antd";
import useShowOrder from "./hooks/useShowOrder";
import useAddToOrder from "./hooks/useAddToOrder";
import useDeleteFromOrder from "./hooks/useDeleteFromOrder";
import useSearchProduct from "./hooks/useSearchProduct";
import useRemoveFromOrder from "./hooks/useRemoveFromOrder";

// This component is used to show each item in the cart.
function CartItem({ id, quantity, setTotalPrice }) {
  const { refetch } = useShowOrder();
  const {mutate: add} = useAddToOrder(refetch);
  const {mutate: subtract} = useDeleteFromOrder(refetch);
  const {mutate: remove} = useRemoveFromOrder(refetch);
  const {data: product} = useSearchProduct(id);

  useEffect(()=> {
      if (product?.Data?.price) {
        setTotalPrice((prev) => ({
              ...prev,
              [id]: quantity*product.Data.price
          }))
      }
      return () => setTotalPrice((prev) => ({...prev, [id]: undefined}));
  }, [id, setTotalPrice, quantity, product])

  return (
    <Card style={{ width: 500, marginBottom: 16, marginTop: 16 }}>
      <p>
        <strong>Name:</strong> {product?.Data?.product_name}
      </p>
      <p>
        <strong>Quantity:</strong> {quantity}
      </p>
      <p>
        <strong>Total Price:</strong> ${quantity*product?.Data?.price}
      </p>
      <Row>
        <Button
          onClick={() => add(id)}
          type="default"
          style={{ marginRight: 16 }}
        >
          +
        </Button>
        <Button
          onClick={() => subtract(id)}
          type="default"
          style={{ marginRight: 16 }}
        >
          -
        </Button>
        <Button
          onClick={() => remove({ productId: id, quantity: quantity })}
          type="primary"
          danger
          style={{ marginRight: 16 }}
        >
          Remove
        </Button>
      </Row>
    </Card>
  );
}

export default CartItem;
