'use client'

import { FoodItemType } from '@/app/types/food'
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
        <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-6'>
            {items &&
                items.map((item, index) => (
                    <div
                        key={index}
                        className='bg-white rounded-lg shadow-lg overflow-hidden transform transition-transform hover:scale-105 m-2 cursor-pointer'
                        onClick={() => window.open(item.url, '_blank')}
                    >
                        <div className='relative h-40'>
                            <Image
                                src={item.image}
                                alt={item.name}
                                layout='fill'
                                objectFit='contain'
                                className='w-full h-full object-cover'
                            />
                        </div>
                        <div className='p-4'>
                            <h4 className='font-semibold mb-2'>{item.name}</h4>
                            <p className='text-gray-600 mb-4'>
                                {truncateText(item.description, 100)}
                            </p>
                            <div className='mt-4 text-center'>
                                <p className='text-xl font-bold text-green-600'>
                                    ${item.price}
                                </p>
                            </div>
                        </div>
                    </div>
                ))}
        </div>
    )
}
export default FoodItem
