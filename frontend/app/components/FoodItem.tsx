'use client'

import { FoodItemType } from '@/app/types/food'
import { Tooltip } from 'antd'
import Image from 'next/image'
import React from 'react'

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
        <div className='grid grid-cols-4 gap-6 p-6'>
            {items?.length !== 0 ? (
                items.map((item, index) => (
                    <Tooltip title={item.description} key={index}>
                        <div
                            className='bg-white shadow-md rounded-lg overflow-hidden transform transition-transform hover:scale-105 cursor-pointer'
                            onClick={() => window.open(item.url, '_blank')}
                        >
                            <div className='relative h-40'>
                                <Image
                                    src={item.image}
                                    alt={item.name}
                                    layout='fill'
                                    objectFit='contain'
                                    className='w-full h-full'
                                />
                            </div>
                            <div className='p-4'>
                                <h3 className='text-green-700 text-xl font-bold'>
                                    ${item.price}
                                </h3>
                                <h4 className='font-semibold text-lg mt-2'>
                                    {item.name}
                                </h4>
                            </div>
                        </div>
                    </Tooltip>
                ))
                ) : (
                    <span className='text-gray-600'>
                        No products found.
                    </span>
                )
            }

        </div>
    )
}
export default FoodItem
