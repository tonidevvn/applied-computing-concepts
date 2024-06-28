import { FoodItemType } from '@/app/types/food'
import Image from 'next/image'
import React from 'react'
import { Card, Row, Col } from 'antd'

const { Meta: CardMeta } = Card

// Utility function to truncate text
const truncateText = (text: string, maxLength: number) => {
    if (!text) {
        return ''
    }
    if (text.length <= maxLength) {
        return text
    }
    return text.slice(0, maxLength) + '...'
}

function FoodItem({ items }: { items: FoodItemType[] }) {
    return (
        <Row gutter={[16, 16]} justify='center'>
            {items.map((item, index) => (
                <Col key={index} xs={24} sm={12} md={8} lg={6} xl={4}>
                    <Card
                        hoverable
                        style={{ width: '100%', height: '100%' }}
                        cover={
                            <div
                                style={{
                                    height: 200,
                                    overflow: 'hidden',
                                    position: 'relative',
                                }}
                            >
                                <Image
                                    src={item.image}
                                    alt={item.name}
                                    layout='fill'
                                    objectFit='cover'
                                />
                            </div>
                        }
                    >
                        <CardMeta
                            title={item.name}
                            description={truncateText(item.description, 100)} // Adjust the maxLength as needed
                        />
                        <div style={{ marginTop: 16, textAlign: 'center' }}>
                            <p
                                style={{
                                    fontWeight: 'bold',
                                    fontSize: '1.2em',
                                }}
                            >
                                ${item.price}
                            </p>
                        </div>
                    </Card>
                </Col>
            ))}
        </Row>
    )
}

export default FoodItem
